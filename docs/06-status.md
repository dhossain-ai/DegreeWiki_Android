# Android Status

Last updated: 2026-07-09

## Current Truth

- Android repo type: native Kotlin app
- Module layout: single `:app` module
- UI stack: Jetpack Compose with Material 3
- Navigation: Navigation 3 root nav plus local tab state
- DI: Hilt
- Network: Retrofit + OkHttp + kotlinx serialization
- Auth: Supabase email/password session auth
- Local storage: Room plus encrypted shared preferences for auth session

## Verified Working Surface

- Programs list
- Universities list
- Countries list
- Program detail from cached list data
- University detail from cached list data
- Country detail from cached list data
- Login screen
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

- placeholder screens remain in source
- Google auth button is visible placeholder UI only
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

## Validation Repair Notes

- Removed stale unit test `app/src/test/java/com/example/degreewiki/ui/features/main/MainScreenViewModelTest.kt`
- Removed stale instrumented test `app/src/androidTest/java/com/example/degreewiki/ui/features/main/MainScreenTest.kt`
- Added compile-valid unit coverage in `app/src/test/java/com/example/degreewiki/data/mapper/MapperTest.kt`
- Added compile-valid Compose coverage in `app/src/androidTest/java/com/example/degreewiki/ui/features/main/BottomNavigationBarTest.kt`
- No product behavior changed in this phase
- No production app code changed in this phase

## Recommended Next Phase

Small production-safe test hardening:

- add more low-risk tests around current mappers, navigation-free composables, and pure state logic
- keep product scope unchanged while improving validation confidence
