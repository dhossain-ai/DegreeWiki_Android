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

## Bundle 4 Status

- Bundle 4 state: completed
- Home remains the first bottom-navigation tab
- Current bottom navigation: `Home / Programs / Universities / Countries / Profile`
- Detail screens still use the shared design foundation introduced in Bundle 2
- Public browse flow from Bundle 1 through Bundle 3 is preserved

## What Improved In Bundle 4

- `DataRepository` now tracks public refresh status explicitly instead of swallowing failures.
- Programs, Universities, Countries, and Home now distinguish between:
  - loading with no cache
  - cached success
  - cached success plus refresh warning
  - no-cache refresh failure
- Public list screens now keep cached content visible and show a subtle retryable warning when refresh fails.
- Public list screens now show clearer full-page retry states when no cached data exists and refresh fails.
- Home now shows a retryable warning when any public catalog refresh fails while cached data is still available.
- Added low-risk test coverage for the friendly detail unavailable state copy.

## What Remains Basic Or Incomplete

- Program detail can still show only the currently cached program fields: title, university, country, degree level, subject, duration, and tuition when present.
- University detail still lacks official website data because that field is not available in the current Android model or API contract.
- Country detail currently focuses on summary plus related cached universities/programs and does not include richer visa or cost content.
- Home still does not implement real advanced search, Fit Finder, chat, scholarships, or guides.
- Upstream text anomalies are still handled as source-data issues rather than aggressively sanitized in-app.

## Validation Results

- `./gradlew.bat test`
  passed
- `./gradlew.bat build`
  passed
- `./gradlew.bat lint`
  passed

## Manual QA Results

- Verified on connected emulator `emulator-5554`
- App opens and Home appears first
- Bottom navigation shows `Home / Programs / Universities / Countries / Profile` without truncating the countries label
- Programs list loads and program detail opens
- Universities list loads and university detail opens
- Destinations list loads and country detail opens
- Back navigation from program, university, and country details returns safely
- Profile opens and the fake Google sign-in button did not return
- Home still presents Fit Finder and chat only as deferred/non-working product messaging
- With network disabled and cached data present, Home and public lists stayed visible and showed a friendly refresh warning instead of crashing
- With network disabled and app data cleared, public screens showed a clear retryable error state instead of blank or misleading content
- No stack traces, raw exception text, or fake data appeared in the UI

## Recommended Next Bundle

Bundle 5 should focus on low-risk public data quality polish, especially upstream text normalization strategy, targeted state coverage, and any remaining screen-level inconsistencies that can be fixed without expanding the Android API surface.
