# Android Architecture

Last audited: 2026-07-09

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

Important limits:

- `Login` nav key exists but is not used in the root nav graph.
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

Observed inconsistency:

- `ProfileScreen` uses plain `collectAsState()` instead of lifecycle-aware collection.

## Repository Layer

- `DataRepository`
  programs, universities, countries, and refresh methods
- `AuthRepository`
  auth state, login, logout
- `ProfileRepository`
  profile fetch, saved-items refresh, saved-item delete

Observations:

- `DataRepository` swallows refresh exceptions with `printStackTrace()`.
- Because refresh failures do not propagate, list screens can stay in `Success(emptyList())` rather than a true error state.

## Local Data Layer

Room database tables:

- `programs`
- `universities`
- `countries`
- `saved_items`

Notes:

- DB version is `2`
- `fallbackToDestructiveMigration(true)` is enabled
- No schema export

Implications:

- local data can be wiped on incompatible schema changes
- current setup favors velocity over migration safety

## Network Layer

- Retrofit interface: `DegreeWikiApiService`
- JSON parser: kotlinx serialization `Json`
- OkHttp auth interceptor attaches Supabase bearer token when available
- OkHttp logging interceptor is enabled at `BODY` level for all builds in current code

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

- existing unit and instrumented tests are stale
- test files reference removed or nonexistent screen/viewmodel contracts
- tests are currently a repo health issue, not a reliable safety net
