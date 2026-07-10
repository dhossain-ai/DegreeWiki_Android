# Android Task Log

## 2026-07-10 - Bundle 4 Public Data Reliability + Error State Polish

- Scoped work to public refresh reliability and honest state handling only.
- Added repository-owned `PublicRefreshState` flows for programs, universities, and countries so refresh failures are no longer silently swallowed.
- Kept cache-first Room rendering intact while exposing refresh failure state to ViewModels.
- Updated Home plus the Programs, Universities, and Countries browse ViewModels to distinguish loading, cached success, cached success with warning, and no-cache error cases.
- Added a shared `RefreshWarningNote` component for subtle retryable cached-data warnings.
- Updated Home and list screens to keep cached data visible when refresh fails and to show full-page retry states only when no cached data exists.
- Added a low-risk instrumented test for friendly detail unavailable-state copy.
- Chose not to add a generic text sanitizer for the upstream encoding artifact because a safe non-lossy rule was not obvious; the issue remains documented as source-data quality.

## Files Created

- `app/src/androidTest/java/com/example/degreewiki/ui/features/details/DetailUnavailableStateTest.kt`

## Files Modified

- `app/src/main/java/com/example/degreewiki/data/repository/DataRepository.kt`
- `app/src/main/java/com/example/degreewiki/ui/components/DegreeWikiComponents.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/DiscoveryUiState.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeViewModel.kt`
- `docs/02-android-architecture.md`
- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`

## Files Deleted

- none

## Intentionally Not Changed

- no Fit Finder implementation
- no chat implementation
- no scholarship or guide feature implementation
- no backend or API endpoint changes
- no dependency additions
- no Room migration changes
- no auth storage redesign
- no network logging changes
- no broad repository architecture rewrite
- no new API fields such as official website, deadlines, languages, or visa content where Android models do not already expose them
- no generic text sanitizer that could risk corrupting legitimate names
- no global offline sync system

## Validation Results

- `./gradlew.bat test` passed
- `./gradlew.bat build` passed
- `./gradlew.bat lint` passed

## Manual QA Results

- Installed the fresh debug APK on emulator `emulator-5554`
- Verified Home renders first
- Verified Programs, Universities, and Destinations lists load
- Verified program, university, and country detail screens still open safely
- Verified back navigation from each opened detail returns safely
- Verified Profile opens and did not reintroduce the fake Google sign-in button
- Verified Home still keeps Chat and Fit Finder in deferred/non-working messaging only
- Disabled emulator Wi-Fi and mobile data with cached data present, relaunched the app, and verified Home plus public browse screens remained visible with a friendly refresh warning
- Cleared app data while offline, reopened the app, and verified public no-cache surfaces showed retryable error states instead of blank content
- Verified no stack traces, raw exception text, or fake data appeared in the UI

## Known Issues

- Auth storage still uses deprecated Android security APIs
- Network logging is still set to `BODY`
- Room still uses destructive migration fallback
- Some upstream text content still contains encoding artifacts in cached program titles, for example the replacement character visible in one related-program title

## Next Recommended Bundle

- Add targeted tests for the new refresh warning/error-state behavior
- Revisit upstream text normalization only with a clearly safe, conservative rule or upstream API fix
