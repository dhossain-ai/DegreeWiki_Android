# Android API Contract

Last audited: 2026-07-11

## Bundle 7 rich public API update

Android continues to parse the three collection endpoints as raw arrays. Their additive fields are nullable in `ProgramDto`, `UniversityDto`, and `CountryDto`, preserving compatibility with older/partial payloads.

Android now calls these wrapped detail endpoints:

- `GET /api/mobile/programs/{slug}` -> `{ ok, item: ProgramDetailDto? }`
- `GET /api/mobile/universities/{slug}` -> `{ ok, item: UniversityDetailDto? }`
- `GET /api/mobile/countries/{slug}` -> `{ ok, item: CountryDetailDto? }`

The detail contracts were verified against the configured backend on 2026-07-11. They include nested identity/location records, public study/cost/admissions/support guidance, official URLs, related records, and explicit verification metadata. Additive properties are nullable; unknown fields are ignored. Detail responses are held in ViewModel state rather than persisted in Room.

## Rule

This file records only contract surface verified from the Android repo and the checked web repo at `W:\DegreeWiki`. This audit made no web repo changes.

## Runtime Base URL

- Base URL comes from `BuildConfig.API_BASE_URL`
- Value is injected from `local.properties`
- Exact environment value is intentionally not stored here

## Verified Android Public Data Usage

### Endpoints actively used by Android public browse flows

`GET /api/mobile/programs`

- Called by `DefaultDataRepository.refreshPrograms()`
- DTO: `ProgramDto`
- Android cache target: `ProgramEntity`
- Used by:
  - Programs list
  - Program detail
  - University detail related-program lookup
  - Country detail related-program lookup

`GET /api/mobile/universities`

- Called by `DefaultDataRepository.refreshUniversities()`
- DTO: `UniversityDto`
- Android cache target: `UniversityEntity`
- Used by:
  - Universities list
  - University detail
  - Country detail related-university lookup

`GET /api/mobile/countries`

- Called by `DefaultDataRepository.refreshCountries()`
- DTO: `CountryDto`
- Android cache target: `CountryEntity`
- Used by:
  - Countries list
  - Country detail
  - University detail country-name resolution

### Endpoints declared in Android but not verified in current public UI flows

`GET /api/mobile/bootstrap`

- Response model:
  - `ok: Boolean`
  - `siteName: String`
  - `featureFlags: Map<String, Boolean>`
- Declared in `DegreeWikiApiService`
- Not verified as used by current Android screens
- Matching route file was not found in checked web repo source during this audit

`GET /api/mobile/programs/search`

- Query params:
  - `q: String?`
  - `page: Int?`
- Response model:
  - `items: List<ProgramSummaryDto>`
  - `page: Int`
  - `pageSize: Int`
  - `total: Int`
  - `nextPage: Int?`
- Declared in `DegreeWikiApiService`
- Not verified as used by current Android screens
- Matching route file was not found in checked web repo source during this audit

### Auth-required endpoints declared in Android

`GET /api/mobile/me`

- Response model:
  - `id: String`
  - `email: String`
  - `name: String`
  - `role: String`
- Used by auth/profile flows, not by current public browse flows
- Matching route file was not found in checked web repo source during this audit

`GET /api/mobile/me/saved-items`

- Response model:
  - list of `SavedItemDto`
- Used by signed-in saved-items flows, not by current public browse flows
- Matching route file was not found in checked web repo source during this audit

`DELETE /api/mobile/me/saved-items/{id}`

- Path param:
  - `id: String`
- Response body:
  - not modeled in client
- Used by signed-in saved-items flows, not by current public browse flows
- Matching route file was not found in checked web repo source during this audit

## Verified DTOs And Cache Shapes

### Program contract

`ProgramDto`

- `id: String`
- `slug: String`
- `title: String`
- `universityName: String`
- `countryName: String`
- `degreeLevel: String`
- `subject: String?`
- `tuition: Double?`
- `duration: String?`

`ProgramEntity`

- `id`
- `slug`
- `title`
- `universityName`
- `countryName`
- `degreeLevel`
- `subject`
- `tuition`
- `duration`
- `offlineSavedAt`

Current Android program rendering:

- List shows: title, university name, country name, degree level, subject, duration
- Detail shows: title, university name, country name, degree level, subject, duration, tuition

Verified missing public fields versus web:

- no university id/slug in Android program DTO
- no country id/code in Android program DTO
- no city/location
- no language
- no study mode
- no delivery mode
- no tuition currency or tuition period
- no application fee
- no intake or deadline data
- no application or official URL
- no requirements fields
- no curriculum or career outcomes
- no verification metadata
- no thumbnail/logo/media URL
- no scholarship cue
- no saved-state flag in public program payload

### University contract

`UniversityDto`

- `id: String`
- `slug: String`
- `name: String`
- `countryId: String`
- `city: String?`
- `logoUrl: String?`
- `overview: String?`

`UniversityEntity`

- `id`
- `slug`
- `name`
- `countryId`
- `city`
- `logoUrl`
- `overview`
- `offlineSavedAt`

Current Android university rendering:

- List shows: name, city, overview fallback text
- Detail shows: name, resolved country name when cached, city, overview, related cached program titles

Verified missing public fields versus web:

- no short name or native name
- no official URL
- no admissions/application URLs
- no verification metadata
- no ranking summary
- no campus summary
- no admissions/international/student-support sections
- no scholarship/housing/language/career support summaries
- no media beyond a single logo URL
- no source confidence or last verified label

### Country contract

`CountryDto`

- `id: String`
- `slug: String`
- `name: String`
- `summary: String?`
- `imageUrl: String?`

`CountryEntity`

- `id`
- `slug`
- `name`
- `summary`
- `imageUrl`
- `offlineSavedAt`

Current Android country rendering:

- List shows: name, summary or fallback text
- Detail shows: name, summary, related cached universities, related cached programs

Verified missing public fields versus web:

- no ISO code
- no capital/currency/language facts
- no tuition or living-cost overview
- no visa/student-work/post-study-work guidance
- no scholarship or intake overview
- no official education or visa URL
- no FAQ payload
- no verification metadata
- no source confidence or last verified label

## Verified Web Mobile Routes

The checked web repo currently contains these public mobile route files:

- `GET /api/mobile/programs`
- `GET /api/mobile/universities`
- `GET /api/mobile/countries`

Verified route behavior:

- `programs.ts` returns a thin list payload shaped exactly for the current Android DTO.
- `universities.ts` returns a thin list payload shaped exactly for the current Android DTO.
- `countries.ts` returns a thin list payload shaped exactly for the current Android DTO.

## Verified Web Richer Public Surfaces

The checked public web pages query substantially richer fields directly from Supabase than the current mobile routes expose.

Highlights:

- Program list pages already use language, location, duration, tuition range, verification status, last verified date, logo/media, and deadline-style display cues.
- Program detail pages additionally use official/application URLs, application fee, admissions requirements, English requirements, GPA requirements, curriculum, career outcomes, and intake/deadline records.
- University list pages already use official URL, ranking summary, verification status, source-check date, and program-count context.
- University detail pages additionally use short/native names, admissions links, language requirement overview, scholarship overview, housing overview, student life, international student support, and career support.
- Destination detail pages additionally use ISO/currency/capital/language facts, tuition and living-cost ranges, visa and work guidance, official URLs, FAQ data, and verification metadata.
- Scholarships and guides have public web list/detail pages, but no matching Android public flow and no verified `src/pages/api/mobile` route files for them.

## Future Endpoint Recommendations

Recommended additions should be new mobile-safe endpoints or expanded mobile payloads, not direct reuse of full web page queries.

Priority 1:

- Add program detail-capable mobile fields:
  - `language`
  - `studyMode`
  - `deliveryMode`
  - `city`
  - `tuitionCurrency`
  - `tuitionPeriod`
  - `officialUrl`
  - `applicationUrl`
  - `verificationStatus`
  - `lastVerifiedAt`

Priority 2:

- Add structured detail fields for:
  - program intakes/deadlines
  - requirements
  - university links and support summaries
  - destination tuition/living/visa/work guidance

Priority 3:

- Add dedicated mobile endpoints for scholarships and guides instead of overloading existing list routes.

## Verified Gaps And Mismatch Risks

- Android public detail screens are cache lookups over list payloads, not true detail endpoints.
- Android declares `/api/mobile/bootstrap` and `/api/mobile/programs/search`, but matching route files were not found in the checked web repo source.
- Android declares `/api/mobile/me` and `/api/mobile/me/saved-items`, but matching route files were not found in the checked web repo source.
- The public web app supports authenticated save actions through `/api/saved-items/program`, which is a different route shape from the Android saved-items contract.
- `UniversityDto.countryId` is useful for cache joins, but Android must resolve it through cached countries because the university payload does not include country name.
- `Json { ignoreUnknownKeys = true }` means additive backend fields will be ignored safely until Android DTOs are updated.
