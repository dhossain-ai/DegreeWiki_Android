# Android Status

Last updated: 2026-07-10

## Current Truth

- Android repo type: native Kotlin app
- Module layout: single `:app` module
- UI stack: Jetpack Compose with Material 3
- Navigation: Navigation 3 root nav plus local tab state
- DI: Hilt
- Network: Retrofit + OkHttp + kotlinx serialization
- Auth: Supabase email/password session auth
- Local storage: Room plus encrypted shared preferences for auth session

## Bundle 1 Status

- Bundle 1 state: in progress
- Public card tap crash: fixed in code and reproduced as resolved for program detail on emulator
- Placeholder surfaces: hidden or quarantined from misleading public presentation
- Product flow map: created at `docs/08-android-product-flow-map.md`
- Current validation: `./gradlew.bat test`, `./gradlew.bat build`, and `./gradlew.bat lint` passed
- Remaining QA gap: full human-driven three-card tap verification for universities and countries should still be repeated because adb coordinate automation was inconsistent after state restoration

## Verified Working Surface

- Programs list
- Universities list
- Countries list
- Program detail from cached list data
- University detail from cached list data
- Country detail from cached list data
- Login screen with email-only sign-in presentation
- Auth-aware profile screen
- Saved-items read and delete flow

## Verified But Not Fully Product-Ready

- Search API contract exists in client but is not wired to UI
- Bootstrap API contract exists in client but is not wired to UI
- Room caching exists but is simple cache persistence, not a full offline product

## Verified Missing Or Deferred

- Native Fit Finder flow
- Native AI chat flow
- Scholarship browsing
- Dedicated detail API fetching
- Save-item create flow
- Route-level login flow
- Real search/filter/sort UI
- Shared design-system components for trust/loading/error states

## Known Problems

- full three-card manual tap verification for universities and countries still needs a human pass on emulator or device
- repository refresh methods swallow exceptions, weakening error reporting
- auth storage implementation uses deprecated Android security APIs
- network logging is set to `BODY` in current app code
- database uses destructive migration fallback

## Docs Status

- `docs/webapp-handoff/` is historical context only
- older copied status/task-log files are web-repo status, not Android app status
- the `docs/00-07` Android docs are now the active Android truth set

## Validation Snapshot

- `./gradlew.bat build`
  passed
- `./gradlew.bat test`
  passed
- `./gradlew.bat lint`
  passed

## Bundle 1 Notes

- Reproduced the original public browse crash from logcat before fixing it
- Updated Navigation 3 detail routing to pass typed nav keys into Hilt view models instead of assuming an `id` in `SavedStateHandle`
- Added student-friendly unavailable states and trust copy on detail screens
- Removed the fake Google sign-in button from the visible login flow
- Reworded placeholder chat, Fit Finder, and discover surfaces so they no longer present as fake product features
- Added the Android product flow map doc for future bundles

## Recommended Next Phase

Bundle 2 should finish public browse hardening:

- complete a human-driven university and country tap sweep on device or emulator
- add low-risk coverage around detail-route creation and empty-state rendering
- decide whether to rename the Countries tab to Destinations in a future product-alignment bundle
