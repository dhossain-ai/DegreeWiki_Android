# Android Status

Last updated: 2026-07-15

## Bundle 13 Status — Scholarships + Guides Android

- Status: implemented and validated.
- Home now has active Scholarship and Guide entry cards; typed list/detail destinations use normal
  back-stack behavior while bottom navigation remains Home, Programs, Universities, Countries,
  Profile.
- Android consumes the raw Scholarship/Guide lists and wrapped `{ ok, item }` details documented by
  Bundle 12 with nullable DTOs, separate domain models, and safe mapping.
- Room database version 3 adds list-summary caches through explicit additive `MIGRATION_2_3` tables;
  existing Program, University, Country, saved-item, and auth-adjacent data is preserved.
- Scholarship browse/detail uses real funding, deadlines, relationships, official links, and
  verification data only. Past/future dates are parsed defensively; unsupported status and absent
  application-process fields are never invented.
- Guide browse/detail renders `structured_blocks_v1` natively with headings, paragraphs, ordered and
  unordered lists, emphasis, safe HTTP(S) links, and related-guide navigation. Unknown blocks are
  skipped, unsafe links become plain text, and no HTML/WebView/Markdown execution is used.
- Cached lists remain visible after refresh failure; cached summaries remain useful when rich detail
  fails. UI never exposes raw exceptions or API/cache terminology.

## Bundle 13 Validation

- `.\gradlew.bat test`: passed
- `.\gradlew.bat build`: passed
- `.\gradlew.bat lint`: passed
- Focused `testDebugUnitTest compileDebugAndroidTestKotlin`: passed before final validation
- Focused tests cover list/wrapped DTO parsing, optional fields, past/future/missing/malformed
  deadlines, supported/unknown Guide blocks, safe/unsafe links, Home entry navigation, structured
  content, and related-guide navigation.
- `connectedDebugAndroidTest` was not run because focused Compose tests compiled and the requested
  behavior did not require a broader emulator matrix.
- The 2026-07-14 backend `500` blocker is resolved. Live Pixel 8 emulator QA against production
  returned the Romanian Government Scholarship and all three published guides. Scholarship and
  guide details loaded with their real slugs; the guide rendered real headings, paragraphs, and
  unordered list content through long scrolling without a crash.
- The existing pre-Bundle-13 installation upgraded in place with `degreewiki.db` retained; no app
  data was cleared. Program data remained available after the migration. Scholarship and guide
  list records were written to Room and remained visible after a forced offline launch; the shared
  refresh warning was observed on cached browse content.
- Home, Programs, Universities, Countries, Profile, search/filter entry points, and the unchanged
  five-item bottom navigation remained available. No live-data defect required an Android code fix.

## Bundle 11 Status — Search + Filter UX

- Status: completed and validated.
- Home accepts a real program query and routes to Programs with it applied.
- Programs supports offline local search, derived multi-select filters, removable active chips, clear actions, reliable text sorting, result counts, and discovery-specific empty states.
- Universities and Study destinations have compact local search using only their current list fields.
- Search/filter state is ViewModel-owned and remains active while opening a detail and returning in the current app session.
- No web, backend endpoint, dependency, Room schema, repository, auth, network logging, Saved, Compare, Scholarships/Guides, Fit Finder, or Chat behavior changed.

## Bundle 11 Validation

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `gradlew.bat connectedDebugAndroidTest`: passed; 15 tests on the Pixel 8 API 17 emulator
- Manual emulator QA: passed for Home query transfer, Programs search/filter/chips/sort/state return, University search, destination search, and helpful zero-result state
- Search operated over already loaded Room-backed records without a search-network request; a live offline transition was not forced

## Bundle 10 Status — Public Detail Screen Redesign

- Status: completed and validated.
- Program, University, and Study destination details now use generic toolbars, concise heroes, key facts before narrative, conditional sections, safe external actions, and compact source status.
- Long narrative content is expandable; FAQ answers are collapsed by default.
- Related cached programs and universities navigate through their existing IDs when available.
- Redundant bottom back actions, raw URL text, duplicate overviews, and student-visible developer copy were removed.
- No web, API, dependency, Room, repository, auth, network logging, offline storage, Save, Compare, Scholarships/Guides flow, Fit Finder, or Chat behavior changed.

## Bundle 10 Validation

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `gradlew.bat connectedDebugAndroidTest`: passed; 13 tests on the Pixel 8 API 17 emulator
- Manual emulator QA: passed for program, university, and country hierarchy; expandable content; safe browser handoff; source status; related-content presentation; and back navigation
- Cached/network fallback implementation was preserved; a live malformed or offline response was not forced during manual QA

## Bundle 9 Status — Home + Public List Redesign

- Status: completed and validated.
- Home now uses student-first identity, search, browse, conditional discovery, deferred Fit Finder, and compact trust sections.
- Programs, Universities, and Study destinations use reusable compact browse cards with constrained text and optional facts.
- Search safely routes to Programs; advanced search is not implied or implemented.
- Existing detail navigation, cached/offline presentation, refresh warnings, errors, and Profile/login flow are preserved.
- No web, API, dependency, Room, repository, auth, network logging, Scholarships, Guides, Fit Finder, Chat, Saved Items, or Compare behavior changed.

## Bundle 9 Validation

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `connectedDebugAndroidTest`: passed, 6 tests on the Pixel 8 API 17 emulator
- Manual emulator QA: passed for Home, search routing, public lists, program detail navigation, back navigation, bottom navigation, and text constraints
- Offline/error fallback code paths were preserved and automated refresh/state behavior remained unchanged; a live offline failure was not forced during manual QA

## Bundle 8 Status — Mobile UX Blueprint

- Status: completed in documentation on 2026-07-12.
- Created the designer-level mobile UX blueprint for the stable Android app.
- Defined student-first copy, mobile navigation, Home, browse lists, details, Profile/login, Fit Finder, Chat, Scholarships, and Guides placement.
- Aligned the Android design system and product flow map with the blueprint.
- No Kotlin, Gradle, Android app, Room/cache/repository, auth, API, dependency, or web repo files were changed.
- No UI, Scholarships/Guides, Fit Finder, or Chat behavior was implemented.

## Current Product And Data State

- Home-first shell and bottom navigation are available: Home, Programs, Universities, Countries, Profile.
- Programs, universities, and countries load from public collection APIs into the existing Room-backed browse flow.
- Detail screens render cached records first and enrich them with richer public detail API responses held in ViewModel memory.
- Cached content remains visible if rich detail loading fails.
- Login works through supported credentials; fake Google sign-in is not present.
- Chat and Fit Finder are not exposed as fake working tabs.
- Scholarships and Guides are active public Home-entry browse/detail flows with Room list caching.

## Bundle 8 Validation

- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- Final diff review confirmed that Bundle 8 changes are documentation-only.

## Known Product Gaps

- Current screen copy and hierarchy still reflect developer-MVP patterns and have not yet been redesigned in code.
- Browse cards need shorter text, better comparison facts, and stronger visual hierarchy.
- Rich detail sections need clearer structure and actionable official links.
- Saved-items product UX, Fit Finder, and Chat remain future work.
- The open UX decisions in `docs/10-android-mobile-ux-blueprint.md` require resolution during the relevant implementation bundles.

## Next Recommended Bundle

Bundle 14 — Profile/Saved Items. Bundle 13 has passed its live production QA gate.
