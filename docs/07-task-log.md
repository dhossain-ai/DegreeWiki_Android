# Android Task Log

## 2026-07-15 — Bundle 13.1: Scholarship + Guide Live Production QA

- Installed Bundle 13 over the existing Pixel 8 emulator installation without clearing data. The
  pre-existing `degreewiki.db` remained present across the Room version-3 install, preserving the
  earlier Program/University/Country cache while adding the Scholarship/Guide summary tables.
- Production QA passed for `GET /api/mobile/scholarships`, its Romanian Government Scholarship
  detail, `GET /api/mobile/guides`, and the Erasmus Mundus guide detail. The device showed the
  real scholarship provider, summary, funding amount, future deadline, verification label, and
  optional-section omission correctly. There is no live related-guide row in the current response,
  so no related-guide action was displayed to exercise.
- All three published guides rendered in the list. The real long-form guide displayed headings,
  paragraphs, emphasis/list content, and safe text content through repeated scrolling without a
  crash or developer-facing error.
- After a successful live load, forced offline launch retained cached browse data and displayed the
  shared refresh-failure warning on cached content. No app data was cleared during this check.
- Regression spot-check passed for Home, Program browse/search cache, bottom navigation, and
  remaining public/profile entry points. No Android code defect was found; no test or dependency
  change was needed.

### Files Modified

- `docs/06-status.md`
- `docs/07-task-log.md`

### Remaining Issues

- None blocking Bundle 14. The current production guide data does not include an eligible related
  guide for a live related-navigation interaction; existing focused Compose coverage remains the
  coverage for that conditional path.

## 2026-07-14 — Bundle 13: Scholarships + Guides Android

- Added exact nullable Scholarship/Guide list and wrapped-detail DTOs from the read-only Bundle 12
  web contract, plus separate domain models and defensive mapping.
- Added tolerant structured Guide blocks: supported headings, paragraphs, ordered/unordered lists,
  strong/emphasis, and safe HTTP(S) links map to native models; unknown blocks/inlines are skipped
  and unsafe links become plain text.
- Added Room `scholarships` and `guides` list-summary tables, DAOs, repository flows/refresh states,
  and explicit additive database migration `2 -> 3` without dropping existing tables.
- Added active Home cards and typed `Scholarships`, `ScholarshipDetail(slug)`, `Guides`, and
  `GuideDetail(slug)` navigation while preserving the five existing bottom tabs.
- Added polished cache-first Scholarship/Guide lists with loading, warning, error, retry, one-record,
  and empty-state behavior; no search, filters, fake featured content, or image dependency.
- Added Scholarship detail using the Bundle 10 hierarchy, safe official/apply actions, conditional
  facts/sections, cautious deadline/verification presentation, and cached-summary fallback.
- Added Guide detail with native article rendering, compact related-guide navigation, source status,
  and cached-summary fallback. No WebView, executable HTML, or Markdown execution was introduced.
- Kept web/backend, auth storage, network logging, Saved/Compare, Fit Finder, Chat, bottom navigation,
  and existing Program/University/Country behavior out of scope.

### Files Created

- Scholarship/Guide DTO, domain, Room entity, DAO, mapper, list/detail ViewModel, screen, deadline,
  card, and guide-renderer files under `app/src/main/java/com/example/degreewiki/`
- `app/src/test/java/com/example/degreewiki/data/network/ScholarshipGuideDtoParsingTest.kt`
- `app/src/test/java/com/example/degreewiki/data/mapper/GuideMapperTest.kt`
- `app/src/test/java/com/example/degreewiki/ui/features/scholarships/ScholarshipDeadlineTest.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/features/guides/GuideDetailContentTest.kt`

### Files Modified

- Network service, Room database/module, data repository, Home/Main/navigation Kotlin files
- `HomeContentTest.kt`
- Android architecture, API contract, design system, status, task log, product flow map, and mobile
  UX blueprint docs

### Files Deleted

- none

### Validation Results

- `.\gradlew.bat test`: passed
- `.\gradlew.bat build`: passed
- `.\gradlew.bat lint`: passed
- Focused unit tests and Android-test Kotlin compilation passed.
- `connectedDebugAndroidTest` was intentionally not run; focused Compose coverage was proportional
  for Home/related navigation and structured content.

### Manual And API QA

- Static/navigation review confirmed active Home entries, typed list/detail/related routes, normal
  back callbacks, and unchanged bottom navigation.
- Unit/Compose coverage verified deadline behavior, optional omission, supported/unknown content,
  unsafe-link handling, Home navigation, native article content, and related-guide navigation.
- Configured backend checks returned HTTP 500 for both `/api/mobile/scholarships` and
  `/api/mobile/guides`; real-record list/detail, live offline transition, browser handoff, and visual
  scrolling QA could not be completed against that environment in this run.

### Remaining Issues

- The configured public backend must deploy/fix the Bundle 12 routes before end-to-end real-data
  device QA can be completed.
- The explicit migration is compiled and schema-matched, but no Room migration instrumentation test
  was added because the repo has no Room-testing dependency and this bundle adds no dependency.
- Remote cover images remain optional initials fallbacks because no image dependency was added.

### Next Recommended Bundle

- First complete a short real-data device QA pass after the configured Scholarship/Guide routes
  return 200. Then proceed to Bundle 14 — Profile/Saved Items without expanding this public browse
  bundle into Saved, Compare, Fit Finder, or Chat.

## 2026-07-13 — Bundle 11: Search + Filter UX

- Added pure local matching, derived filter options, multi-filter combination, and reliable program sorting.
- Upgraded Home search to accept text, clear it, submit from the keyboard or button, and open Programs with the query applied once.
- Added compact search bars, filter-count action, draft/apply filter sheet, active removable chips, sort sheet, natural result counts, and helpful zero-result states.
- Added simple local University and Study destination search using only current list/domain fields.
- Kept search/filter/sort state in ViewModels so it survives detail navigation and tab recomposition within the session.
- Did not call or implement `/api/mobile/programs/search`.

### Files Created

- `app/src/main/java/com/example/degreewiki/ui/features/discover/BrowseSearch.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramFilterSheets.kt`
- `app/src/main/java/com/example/degreewiki/ui/components/SearchComponents.kt`
- `app/src/test/java/com/example/degreewiki/ui/features/discover/BrowseSearchTest.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/components/SearchComponentsTest.kt`

### Files Modified

- `app/src/main/java/com/example/degreewiki/ui/components/BrowseComponents.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/home/HomeScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/main/MainScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/ProgramsViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/UniversitiesViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/discover/CountriesViewModel.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/features/home/HomeContentTest.kt`
- `docs/03-android-api-contract.md`
- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`
- `docs/10-android-mobile-ux-blueprint.md`

### Intentionally Not Changed

- web repo, backend routes, DTO/API payloads, Room schema, repositories, auth storage, network logging, and dependencies
- detail-screen design, Saved, Compare, Scholarships/Guides, Fit Finder, Chat, remote autocomplete, and fake suggestions
- unsupported list filters for language, study mode, delivery, deadline, verification, and tuition range

### Validation Results

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `gradlew.bat connectedDebugAndroidTest`: passed; 15 tests ran with no failures on the Pixel 8 API 17 emulator
- Gradle emitted the existing SDK XML version compatibility warning during `test`; validation still succeeded

### Manual QA

- Home query entry and one-time transfer to Programs passed; the emulator stylus tutorial obscured the first IME attempt, while the Compose test verifies the same controlled search action.
- Program title/subject search, multi-filter draft/apply, filter count, individual chip removal, clear-all affordance, Title A–Z sorting, detail/back state retention, and natural result counts passed.
- University name search and destination name search passed with real cached records.
- Helpful zero-result content and clear-search action passed.
- Bottom navigation, detail navigation, refresh-state code paths, and existing browse cards remained stable.
- Search used the loaded Room-backed collections; a live offline transition was not forced, and no search endpoint is called by this flow.

### Remaining Issues

- University country names, program language/modes/deadlines, destination continent/currency, and comparable tuition currencies are unavailable in current list models and therefore are not searchable/filterable.
- Search state is session-scoped ViewModel state rather than durable cross-process persistence.
- University filter options are hidden when the derived list exceeds twelve entries to keep the filter sheet usable.

### Next Recommended Bundle

- Bundle 12 — Scholarships/Guides public API work in the web repo.

## 2026-07-12 — Bundle 10: Public Detail Screen Redesign

- Rebuilt the shared detail component system around generic toolbars, concise heroes, safe external actions, compact facts, bordered sections, expandable narrative text, related-content rows, source status, and FAQ accordions.
- Reordered Program detail around official/apply actions and decision facts before admission and narrative content.
- Removed duplicate university overview content and made real cached related programs tappable.
- Moved country planning content before the long destination narrative and added tappable related universities/programs plus FAQ accordions.
- Removed raw URLs, bottom back buttons, and student-visible developer/cache copy.
- Changed related-content ViewModel state from display strings to existing domain objects so real IDs remain available for navigation; persistence and repository behavior did not change.

### Files Created

- `app/src/androidTest/java/com/example/degreewiki/ui/features/details/DetailComponentsTest.kt`

### Files Modified

- `app/src/main/java/com/example/degreewiki/ui/features/details/DetailScreenComponents.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/ProgramDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/UniversityDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/CountryDetailScreen.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/UniversityDetailViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/details/CountryDetailViewModel.kt`
- `app/src/main/java/com/example/degreewiki/ui/navigation/Navigation.kt`
- `docs/04-android-design-system.md`
- `docs/06-status.md`
- `docs/07-task-log.md`
- `docs/08-android-product-flow-map.md`
- `docs/10-android-mobile-ux-blueprint.md`

### Intentionally Not Changed

- web repo, APIs, DTO contract, Room schema, repositories, auth storage, network logging, and dependencies
- Save, Compare, Scholarships/Guides flows, Fit Finder, Chat, and image loading
- list navigation, Home-first shell, Profile/login, and bottom navigation
- fake or placeholder data

### Validation Results

- `gradlew.bat --stop`: passed
- `gradlew.bat test`: passed
- `gradlew.bat build`: passed
- `gradlew.bat lint`: passed
- `gradlew.bat connectedDebugAndroidTest`: passed; 13 tests ran with no failures on the Pixel 8 API 17 emulator
- Gradle emitted the existing SDK XML version compatibility warning during `test`; validation still succeeded

### Manual QA

- Program: generic toolbar, concise hero, external actions, facts before content, raw-URL removal, expandable curriculum, cautious source status, and no bottom back button passed.
- University: single About overview, text expansion, compact related-program rows, generic toolbar, and no developer copy passed.
- Country: planning hierarchy, single non-duplicated About overview, expandable narrative, generic toolbar, and cautious source status passed. Manual review caught and fixed an identical hero/About summary duplication before final validation.
- External-link handoff opened the emulator browser without crashing; unsafe/missing URL omission is covered by connected tests.
- FAQ collapse/expand, missing-section omission, overview deduplication, related-program callback, and source-status wording passed connected Compose tests.
- Back arrow and system back remained functional. No raw IDs, raw URLs, fake data, or bottom back actions were visible.
- A live malformed-response/offline failure was not forced; the existing repository and cached fallback behavior was unchanged.

### Remaining Issues

- Related navigation is available only for records present in the existing cached collections with real IDs.
- Remote hero/logo images remain out of scope because the app has no image-loading dependency.
- External URI handling is failure-safe, but Android/browser availability controls the final handoff outside the app.

### Next Recommended Bundle

- Bundle 11 — Search + Filter UX.

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
# 2026-07-16 — Bundle 15: Profile + Saved Programs

- Read `W:\DegreeWiki\docs\11-mobile-api.md` read-only and implemented its exact authenticated
  Profile/Saved Program contract in Android.
- Restricted bearer injection to marked protected requests and redacted the authorization header.
- Replaced obsolete Profile/Saved DTOs, domain models, repository behavior, and Room saved-item
  shape with account-partitioned Saved Program data.
- Added benefit-first logged-out Profile, genuine logged-in account summary, typed Login and Saved
  Programs destinations, shared Save/Saved controls, login prompt, empty/error states, and logout.
- Added focused tests for protected/public headers, profile nullability, saved response parsing,
  Program ID mapping, duplicate collapse, logout clearing, 401 classification, login prompt, empty
  saved list, and Save/Saved UI state.

### Files Created

- `app/src/main/java/com/example/degreewiki/ui/components/SavedProgramComponents.kt`
- `app/src/main/java/com/example/degreewiki/ui/features/profile/SavedProgramsScreen.kt`
- `app/src/test/java/com/example/degreewiki/data/network/AuthInterceptorTest.kt`
- `app/src/test/java/com/example/degreewiki/data/network/AuthenticatedDtoParsingTest.kt`
- `app/src/test/java/com/example/degreewiki/data/repository/SavedProgramsStateTest.kt`
- `app/src/androidTest/java/com/example/degreewiki/ui/components/SavedProgramComponentsTest.kt`

### Files Modified

- authenticated network/session, DTO, repository, domain, Room, navigation, auth, Home, Programs,
  Program detail, Profile, Gradle test dependency, and all Bundle 15 documentation files

### Files Deleted

- none

### Intentionally Not Changed

- no web repo files or backend endpoints
- no Saved Universities, Countries, Scholarships, or Guides
- no Compare, Fit Finder, or Chat
- no registration or social login
- no offline mutation queue
- no unrelated public-screen redesign
- no public Program, University, Country, Scholarship, or Guide cache deletion on logout

### Validation Results

- focused `:app:testDebugUnitTest :app:compileDebugAndroidTestKotlin`: passed
- `.\gradlew.bat test`: passed
- `.\gradlew.bat build`: passed
- `.\gradlew.bat lint`: passed
- connected tests: not run; focused Compose coverage compiled and no emulator matrix was required
- final validation emitted the existing AndroidX Security deprecation warnings and non-failing
  native symbol-strip notices

### Manual QA

- Static/code-path QA completed for logged-out benefit flow, typed Login return, profile field
  fallback, shared save state, saved-item deletion mapping, account partitioning, logout clearing,
  session expiry, empty/error copy, and public-request isolation.
- Live account/device QA was not run in this pass; production credentials were not available to the
  automated environment.

### Known Issues

- Existing encrypted-session storage uses deprecated AndroidX Security APIs and still emits the
  pre-existing build warnings.
- Live authenticated production behavior still requires device QA with a real test account.

### Next Recommended Bundle

- Bundle 16 — Fit Finder, only after its backend-scored Program contract is verified.
