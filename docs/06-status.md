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

## Bundle 3 Status

- Bundle 3 state: completed
- Home remains the first bottom-navigation tab
- Current bottom navigation: `Home / Programs / Universities / Countries / Profile`
- Detail screens now use the shared design foundation introduced in Bundle 2
- Public browse flow from Bundle 1 and Bundle 2 is preserved

## What Improved In Bundle 3

- Restyled `ProgramDetailScreen`, `UniversityDetailScreen`, and `CountryDetailScreen` to use the shared shell, white cards, navy headings, slate supporting copy, and trust-note treatment.
- Added omission-first detail rendering so missing fields are skipped instead of showing raw IDs, `null`, or made-up placeholders.
- Added safe polished unavailable states with a student-friendly message and a back action.
- Resolved university country display through existing cached country data when available, and omitted the field when it would otherwise have shown a raw ID.
- Added low-risk related-content sections on university and country details using only current cached Android data.
- Changed the narrow bottom-navigation label from `Destinations` to `Countries` so the label fits cleanly on narrower emulator widths.

## What Remains Basic Or Incomplete

- Program detail can still show only the currently cached program fields: title, university, country, degree level, subject, duration, and tuition when present.
- University detail still lacks official website data because that field is not available in the current Android model or API contract.
- Country detail currently focuses on summary plus related cached universities/programs and does not include richer visa or cost content.
- Home still does not implement real advanced search, Fit Finder, chat, scholarships, or guides.
- Repository refresh methods still swallow exceptions, which limits richer error reporting.

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
- Programs list loads and program detail opens with the polished card-based layout
- Universities list loads and university detail opens with location resolution plus related programs where cached matches exist
- Destinations list loads and country detail opens with the polished shell and trust note
- Back navigation from program, university, and country details returns safely to the prior browse surface
- Profile opens and the fake Google sign-in button did not return
- Home still presents Fit Finder and chat only as deferred/non-working product messaging
- No fake tuition, deadline, source, scholarship, or admissions data was added by this bundle

## Recommended Next Bundle

Bundle 4 should focus on low-risk data-quality and state polish, especially better surfaced refresh failures and improved text handling for upstream content anomalies, without expanding the Android API surface.
