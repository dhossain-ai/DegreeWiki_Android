# Android Architecture

Last audited: 2026-07-16 (Bundle 15)

## Bundle 15 Authenticated Account Architecture

- `AuthRepository` remains the single Supabase email/password session owner.
- `AccessTokenProvider` reads the current Supabase access token only when OkHttp processes a
  Retrofit method marked with `X-DegreeWiki-Authenticated: true`.
- `AuthInterceptor` removes that internal marker before transmission and adds a bearer header only
  to protected mobile routes. Public browse/detail requests remain unauthenticated.
- HTTP logging redacts `Authorization`; tokens are not copied into ViewModel state or UI.
- `ProfileRepository` is the app-wide source of truth for `/api/mobile/me`, saved Programs, the
  Program ID to saved-item ID map, pending mutations, session expiry, and safe student-facing
  failures.
- Home, Programs, Program detail, Profile, and Saved Programs observe the same saved state, so
  confirmed save/unsave changes are reflected across visible Program surfaces.
- `MIGRATION_3_4` replaces only the obsolete `saved_items` table. Saved summaries are partitioned by
  `ownerUserId`, uniquely keyed per account and Program, and cleared from active state on logout.
- Public Program, University, Country, Scholarship, and Guide tables are not cleared on logout.

## High-Level Shape

The app follows a light MVVM-style structure:

- Compose UI screens
- ViewModels per active screen group
- Repositories for auth, content, and profile data
- Retrofit network layer
- Room cache for content and saved items
- Hilt for dependency injection

This is a practical layered app, not a strict clean architecture split with use cases/interactors.

## Entry Points

- `DegreeWikiApplication`
  Hilt application root
- `MainActivity`
  sets Compose content and theme, then starts `MainNavigation`

## Navigation

Verified navigation uses `androidx.navigation3`:

- Root destination: `Main`
- Detail destinations:
  - `ProgramDetail(id: String)`
  - `UniversityDetail(id: String)`
  - `CountryDetail(id: String)`
  - `ScholarshipDetail(slug: String)`
  - `GuideDetail(slug: String)`
  - `SavedPrograms`
- Auth destination:
  - `Login`
- Home-only browse destinations:
  - `Scholarships`
  - `Guides`

Important limits:

- `Login` and `SavedPrograms` are typed root destinations and do not add bottom tabs.
- Main tab switching is local UI state inside `MainScreen`, not route-based navigation.
- Chat and Fit Finder are not part of current navigation.

## UI Layer

Active features:

- `main`
  tab shell and bottom navigation
- `discover`
  programs, universities, countries lists
- `details`
  program, university, country detail screens
- `scholarships`
  public scholarship browse/detail and deadline presentation
- `guides`
  public guide browse/detail and structured native content rendering
- `auth`
  login form and auth state handling
- `profile`
  user profile and saved item list

Inactive placeholder features:

- `discover/DiscoverScreen`
- `chat/ChatScreen`
- `fitfinder/FitFinderScreen`

## State Handling

Patterns in use:

- `StateFlow` from repositories and view models
- `collectAsStateWithLifecycle()` in most active Compose screens
- `rememberSaveable` for transient UI state such as current tab and form fields

Profile, Saved Programs, and active browse/detail surfaces use lifecycle-aware collection.

## Repository Layer

- `DataRepository`
  programs, universities, countries, scholarships, guides, detail fetches, and refresh methods
- `AuthRepository`
  auth state, login, logout
- `ProfileRepository`
  authenticated profile state, account-partitioned saved Program cache, save/unsave mutations,
  session-expiry handling, and shared saved indicators

Observations:

- `DataRepository` owns small `StateFlow` refresh-status channels for programs, universities, countries, scholarships, and guides.
- Each refresh path updates `PublicRefreshState(isRefreshing, lastRefreshFailed)` so ViewModels can distinguish:
  - initial loading with no cached data
  - cached-data success
  - cached-data success plus refresh warning
  - no-cache refresh failure
- Refresh failures are intentionally converted into simple UI-safe state rather than surfacing raw exception messages or stack traces.

## Public Data Refresh Pattern

Public catalog screens now follow a cache-first pattern:

1. Room-backed list flow emits cached content if available
2. ViewModel triggers repository refresh on init or retry
3. Repository updates the matching refresh-status flow
4. ViewModel combines cached data plus refresh status into UI state

UI behavior:

- If cached data exists and refresh fails, the screen stays usable and shows a subtle warning with retry.
- If no cached data exists and refresh fails, the screen shows a full error state with retry.
- Home aggregates the three public refresh-status flows and shows either:
  - a non-blocking refresh warning when any public catalog fails but cached data exists
  - a full-page error only when all visible public content is empty and refresh has failed

## Local Data Layer

Room database tables:

- `programs`
- `universities`
- `countries`
- `saved_items` (authenticated Saved Program summaries partitioned by owner)
- `scholarships` (list summaries only)
- `guides` (list summaries only)

Notes:

- DB version is `4`
- `MIGRATION_3_4` replaces the pre-contract saved-item table with the verified Saved Program shape
  and an account/program unique index.
- `MIGRATION_2_3` creates the Scholarship and Guide list-cache tables without dropping Program,
  University, Country, saved-item, or auth-adjacent data.
- Rich Scholarship and Guide details remain in ViewModel memory. Their ViewModels observe the
  cached list summary by slug and keep it visible when detail loading fails.
- `fallbackToDestructiveMigration(true)` is enabled
- No schema export

Implications:

- local data can be wiped on incompatible schema changes
- current setup favors velocity over migration safety

## Network Layer

- Retrofit interface: `DegreeWikiApiService`
- JSON parser: kotlinx serialization `Json`
- OkHttp auth interceptor attaches Supabase bearer tokens only to explicitly protected methods
- OkHttp logging is enabled at `BODY` level and redacts `Authorization`

## Auth Layer

- Supabase client created in `AuthModule`
- Session storage uses `EncryptedSharedPreferences`
- Secure session manager persists the serialized Supabase session

Known technical note:

- `EncryptedSharedPreferences` and `MasterKeys` usage is deprecated and already triggers build warnings.

## Detail Data Flow Limitation

There are no dedicated repository methods or API calls for program, university, or country detail endpoints.

Current detail flow:

1. list endpoint fetched
2. list payload cached in Room
3. detail screen loads one cached row by `id`

That means:

- deep-link-style detail loading is not independently supported in current architecture
- detail freshness depends on list refresh
- detail screens cannot show richer server detail than the list/cache schema contains

## Tests

Verified test state:

- mapper unit tests pass
- bottom-navigation instrumented coverage exists
- detail unavailable-state instrumented coverage exists
- the test surface is still light and focused on low-risk UI/state checks rather than deep integration coverage
