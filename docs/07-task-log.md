# Android Task Log

## 2026-07-12 — Bundle 9: Home + Public List Redesign

- Rebuilt Home around a compact student-first identity header, safe Programs search entry, three browse routes, real conditional discovery rows, deferred Fit Finder, and a small independence note.
- Replaced developer/internal copy across Home and the three public lists.
- Added reusable program, university, and country browse cards with bounded text and optional metadata rendering.
- Kept `Countries` in bottom navigation and adopted `Study destinations` as the screen heading.
- Preserved Room-backed lists, ID-based detail navigation, refresh warnings, empty/error states, and Profile/login behavior.
- Added Compose tests for Home search navigation and browse-card optional-field behavior.
- Fixed obsolete explicit Compose assertion imports in two existing Android tests; no test dependency changed.

### Files Created

- `app/src/main/java/com/example/degreewiki/ui/components/BrowseComponents.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/components/BrowseComponentsTest.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/features/home/HomeContentTest.kt`

### Files Modified

- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/main/MainScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesScreen.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/features/details/DetailUnavailableStateTest.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/features/main/BottomNavigationBarTest.kt`
- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`
- `docs/10-android-mobile-ux-blueprint.md`

### Intentionally Not Changed

- web repo and public API endpoints
- dependencies and Gradle configuration
- Room schema, cache, repository, auth storage, and network logging
- advanced search, filters, Scholarships, Guides, Fit Finder, Chat, Saved Items, and Compare
- detail-screen implementation
- fake or placeholder data

### Validation Results

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `connectedDebugAndroidTest`: passed; 6 tests ran with no failures on the Pixel 8 API 17 emulator
- Gradle emitted the existing SDK XML version compatibility warning during `test`; validation still succeeded

### Manual QA

- Home identity, search entry, browse cards, natural counts, horizontal featured content, and bottom navigation: passed on emulator.
- Search entry safely opened Programs: passed.
- Program cards were compact; optional facts were omitted; card tap opened rich detail; system back returned to Programs: passed.
- University overview text and destination summaries were visibly constrained; missing overview content produced no technical fallback: passed.
- Universities, Countries, and Profile bottom destinations remained reachable: passed for navigation labels and public lists; auth behavior was unchanged.
- Static prohibited-copy scan across Home, discover screens, and shared components: passed.
- Connected Compose tests covered Home navigation, bottom navigation, detail unavailable state, and optional card fields: passed.
- A live offline failure was not forced; existing Room/repository and refresh-state code was not changed, and refresh warning/error rendering paths remain in place.

### Remaining Issues

- Remote logos and destination images are not rendered because no image-loading dependency exists; initials are used safely.
- List models do not currently expose language, deadlines, public verification, university country names, rankings, or destination cost facts.
- Horizontal discovery rows route to their corresponding browse screen rather than directly to detail.

### Next Recommended Bundle

- Bundle 10 — Detail Screen Redesign.

## 2026-07-12 — Bundle 8: Mobile UX Blueprint

- Created a designer-level, mobile-native UX blueprint to guide Android implementation after technical stabilization.
- Defined product principles, student-facing copy, near-term navigation, visual direction, and screen-by-screen blueprints for Home, public lists, public details, Profile/login, Fit Finder, Chat, Scholarships, and Guides.
- Defined Bundles 9–16 as the recommended implementation sequence.
- Updated the design system with copy rules, list-card text limits, conditional detail-section rules, and the requirement to omit missing data.
- Updated the flow map to reflect the current cache-first plus in-memory rich-detail behavior and current public versus login-gated boundaries.

### Files Created

- `docs/10-android-mobile-ux-blueprint.md`

### Files Modified

- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`

### Files Deleted

- none

### Intentionally Not Changed

- no Kotlin or Android UI code
- no Gradle files or dependencies
- no web repo files
- no API behavior
- no Room, cache, repository, auth, or network logging behavior
- no Scholarships, Guides, Fit Finder, or Chat implementation
- no fake data

### Validation Results

- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- Docs-only scope and changed-file list: passed final diff review
- The sandbox initially blocked the Gradle distribution download; validation succeeded after the configured Gradle distribution was allowed to download.

### Known Issues And Open Decisions

- The current app UI still reflects the pre-blueprint developer-MVP design; this bundle intentionally changes documentation only.
- Navigation naming, featured-content layout, Compare access, Saved navigation, Fit Finder login timing, and first search filters remain open decisions in the blueprint.

### Next Recommended Bundle

- Bundle 9 — Home + Public List Redesign using existing API/cache data only.

## 2026-07-11 - Bundle 5 Public Mobile API/Data Contract Audit

- Scoped work to audit and documentation only.
- Inspected Android public data flow across:
  - `DegreeWikiApiService`
  - public DTOs
  - Room entities and DAOs
  - mappers
  - `DataRepository`
  - Programs, Universities, Countries ViewModels
  - Program, University, Country detail ViewModels
  - Programs, Universities, Countries list screens
  - Program, University, Country detail screens
- Verified Android public detail screens currently render only Room-cached collection payloads and do not call dedicated detail endpoints.
- Inspected sibling web repo at `W:\DegreeWiki` read-only.
- Verified public web pages for programs, universities, destinations, scholarships, and guides query much richer fields than the current Android mobile routes expose.
- Verified current `src/pages/api/mobile` route files only for:
  - `programs.ts`
  - `universities.ts`
  - `countries.ts`
- Did not find matching checked route files for:
  - `/api/mobile/bootstrap`
  - `/api/mobile/programs/search`
  - `/api/mobile/me`
  - `/api/mobile/me/saved-items`
- Created a dedicated audit document with contract tables, gap analysis, readiness notes, and backend/mobile recommendations.

## Files Created

- `docs/09-android-public-data-contract-audit.md`

## Files Modified

- `docs/03-android-api-contract.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`

## Files Deleted

- none

## Web Repo Inspection

- Web repo was available and inspected read-only at `W:\DegreeWiki`
- No web repo files were modified

## Intentionally Not Changed

- no Android app code
- no Android UI fields
- no DTO/entity/schema changes
- no backend/mobile API fields
- no web repo routes
- no scholarships implementation
- no guides implementation
- no Fit Finder implementation
- no chat implementation
- no search/filter implementation
- no dependency additions

## Validation Results

- `./gradlew.bat test` passed
- `./gradlew.bat build` passed
- `./gradlew.bat lint` passed
- `build` showed one Android SDK XML version warning from the local toolchain, but did not fail

## Known Issues

- Android public detail depth is constrained by thin collection payloads
- Android declares some mobile endpoints whose matching checked web route files were not found during this audit
- Public web save behavior uses `/api/saved-items/program`, which does not match the Android `/api/mobile/me/saved-items` contract shape
- Local build output includes an Android SDK XML version warning that appears toolchain-related rather than project-code-related

## Next Recommended Bundle

- expand the public mobile contract for program, university, and destination detail fields
- add Android DTO/cache/rendering support only after those fields are verified in the mobile API
- plan dedicated scholarships and guides mobile endpoints separately from the current browse contract
# 2026-07-11 — Bundle 7: consume rich public browse API

Changed: expanded nullable list DTO contracts; added wrapped detail DTOs and Retrofit calls; added repository failure-safe detail fetches; wired all three detail ViewModels and screens to real rich fields; added parsing compatibility tests; updated API, status, flow, and audit docs.

Created:

- `app/src/main/java/com/example/degreewiki/data/network/dto/PublicDetailDtos.kt`
- `app/src/test/java/com/example/degreewiki/data/network/PublicDtoParsingTest.kt`

Modified: public DTOs, `DegreeWikiApiService`, `DataRepository`, three detail ViewModels, three detail screens, and Bundle 7 documentation. Deleted: none.

Intentionally unchanged: web repo, Room schema/migration behavior, auth storage, network logging, dependencies, scholarships, guides, Fit Finder, and chat.

Validation: `gradlew test`, `gradlew build`, and `gradlew lint` all passed. Manual QA: no emulator/device session was available; live list/detail API response shapes were inspected successfully. Known issue: list additions are not persisted, and official URLs are currently displayed as text rather than launched. Next: device QA and detail interaction polish.
