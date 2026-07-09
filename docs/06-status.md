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

## Bundle 2 Status

- Bundle 2 state: completed
- Home tab added: yes
- Home is now the first bottom-navigation tab
- Current bottom navigation: `Home / Programs / Universities / Destinations / Profile`
- Design-system foundation created: yes, with shared shell/card/state components in Compose
- Public browse flow from Bundle 1 preserved: yes

## What Improved In Bundle 2

- Added a mobile-native Home screen with:
  - DegreeWiki header and student-friendly intro copy
  - search-entry card routed into Programs
  - compact trust/source note
  - real quick-browse entries for Programs, Universities, and Destinations
  - real cache-backed featured items only
  - deferred Fit Finder, scholarship, guide, and question/help messaging without fake implementations
- Restyled public list screens to use the warmer app shell and shared cards.
- Reduced the old generic Material-demo feel across the public browse surface.

## What Remains Basic Or Incomplete

- Detail screens are still only lightly polished and not fully migrated onto the new shared shell layer.
- Bottom-nav `Destinations` text is truncated with ellipsis on narrow emulator width to avoid a two-line wrap.
- Home does not implement real advanced search, Fit Finder, chat, scholarships, or guides.
- Repository refresh methods still swallow exceptions, which limits richer error reporting.

## Validation Results

- `./gradlew.bat test`
  passed
- `./gradlew.bat build`
  passed
- `./gradlew.bat lint`
  passed

## Manual QA Results

- App opens
- Home appears first
- Home shows only real cached data plus clearly deferred entries
- Bottom navigation works for:
  - Home
  - Programs
  - Universities
  - Destinations
  - Profile
- Program list loads
- University list loads
- Destinations list loads
- Program detail opens without crash
- University detail opens without crash
- Country detail opens without crash
- Profile opens
- Fake Google sign-in button did not return
- Chat and Fit Finder are not exposed as fake working product features

## Recommended Next Bundle

Bundle 3 should extend the design foundation into detail screens and improve narrow-device app-shell polish without expanding into fake product surfaces.
