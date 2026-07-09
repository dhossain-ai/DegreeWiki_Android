# Android Task Log

## 2026-07-10 - Bundle 2 Mobile Home + App Shell Design Foundation

- Scope targeted the native mobile shell and public browse presentation only.
- Added a real `Home` first tab to bottom navigation.
- Replaced the generic discover/home placeholder with a student-first public Home screen.
- Introduced shared Compose design-system primitives for shell, cards, headings, trust note, badges, buttons, and loading/error/empty states.
- Restyled public Programs, Universities, and Destinations list screens to use the shared shell.
- Kept Bundle 1 detail navigation and safe cached detail behavior intact.
- Added real cache-backed featured content on Home only where data was already available.
- Kept Fit Finder, chat, scholarships, and guides in clearly deferred or login-oriented presentation only.

## Files Created

- `app/src/main/java/com/example/degreewiki/ui/components/DegreeWikiComponents.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeViewModel.kt`

## Files Modified

- `app/src/androidTest/java/com/example/degreewiki/ui/features/main/BottomNavigationBarTest.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/main/BottomNavigationBar.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/main/MainScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/theme/Color.kt`
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
- no deep redesign of every detail screen

## Validation Results

- `./gradlew.bat test` passed
- `./gradlew.bat build` passed
- `./gradlew.bat lint` passed

## Manual QA Results

- Installed the fresh debug APK on emulator `emulator-5554`
- Verified Home renders first with DegreeWiki heading, trust note, quick browse, and deferred feature messaging
- Verified Programs list loads and first program detail opens without crash
- Verified Universities list loads and first university detail opens without crash
- Verified Destinations list loads and first destination detail opens without crash
- Verified Profile opens and did not reintroduce the fake Google sign-in button
- Verified Chat and Fit Finder are not exposed as fake working flows in the new shell

## Known Issues

- Bottom-nav `Destinations` label is truncated with ellipsis on narrow emulator width to avoid multi-line wrapping
- Detail screens still use older layouts and only received light polish in this bundle
- Repository refresh failures are still swallowed
- Auth storage still uses deprecated Android security APIs
- Network logging is still set to `BODY`
- Room still uses destructive migration fallback

## Next Recommended Bundle

- Extend the new shell to detail screens and improve narrow-device bottom-nav polish
- Add low-risk tests around Home navigation and public empty/error states
