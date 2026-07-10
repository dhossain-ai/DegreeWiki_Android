# Android Task Log

## 2026-07-10 - Bundle 3 Detail Screens + Narrow App Shell Polish

- Scoped work to public detail-screen polish plus the narrow-device bottom navigation issue.
- Rebuilt Program, University, and Country detail screens on top of the shared Bundle 2 shell and card components.
- Added shared detail helper composables for hero cards, fact cards, related-content cards, trust notes, back app bars, and polished unavailable states.
- Kept detail rendering omission-first so missing values are skipped instead of guessed, padded, or exposed as raw IDs.
- Resolved university country names from existing cached country records and suppressed raw `countryId` display when no safe match exists.
- Added related-program and related-university sections only when current cached Android data could derive them safely.
- Changed the bottom navigation label from `Destinations` to `Countries` to fit narrow emulator widths more cleanly.
- Preserved Home-first behavior, public browse loading, safe detail taps, and the absence of fake Chat, Fit Finder, scholarship, guide, or Google sign-in surfaces.

## Files Created

- `app/src/main/java/com/example/degreewiki/ui/features/details/DetailScreenComponents.kt`

## Files Modified

- `app/src/androidTest/java/com/example/degreewiki/ui/features/main/BottomNavigationBarTest.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/CountryDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/CountryDetailViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/ProgramDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/ProgramDetailViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/UniversityDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/UniversityDetailViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/main/BottomNavigationBar.kt`
- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`

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
- no repository architecture rewrite
- no new API fields such as official website, deadlines, languages, or visa content where Android models do not already expose them

## Validation Results

- `./gradlew.bat test` passed
- `./gradlew.bat build` passed
- `./gradlew.bat lint` passed
- An initial parallel validation attempt caused generated-file collisions in Gradle/KSP/Hilt on Windows; rerunning the required commands sequentially after `./gradlew.bat --stop` and `./gradlew.bat clean` passed cleanly

## Manual QA Results

- Installed the fresh debug APK on emulator `emulator-5554`
- Verified Home renders first and the bottom navigation now shows `Countries` without truncation
- Verified Programs list loads and the first program detail opens with the new hero card, facts card, trust note, and back CTA
- Verified Universities list loads and a university detail opens with safe country-name resolution plus related cached programs
- Verified Destinations list loads and a country detail opens with the new shell and trust note
- Verified back navigation from each opened detail returns safely
- Verified Profile opens and did not reintroduce the fake Google sign-in button
- Verified Home still keeps Chat and Fit Finder in deferred/non-working messaging only
- Verified no fake deadline, verification, source, or admissions fields appeared in the new detail views

## Known Issues

- Repository refresh failures are still swallowed
- Auth storage still uses deprecated Android security APIs
- Network logging is still set to `BODY`
- Room still uses destructive migration fallback
- Some upstream text content still contains encoding artifacts in cached program titles, for example the replacement character visible in one related-program title

## Next Recommended Bundle

- Improve refresh/error surfacing and data-quality handling without expanding the API surface
- Add targeted tests for detail unavailable states and cache-derived related-content rendering
