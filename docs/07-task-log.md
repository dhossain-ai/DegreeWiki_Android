# Android Task Log

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
