# Android Public Data Contract Audit

Last audited: 2026-07-11

## Summary

Bundle 5 confirmed that Android public browse is stable but intentionally thin. The app loads real Programs, Universities, and Countries from three mobile collection endpoints, caches them in Room, and renders details only from that cached collection data. The public web app already uses substantially richer program, university, destination, scholarship, and guide data that Android does not currently receive.

Strict rule from this audit:

- no fake data
- no inferred deadlines
- no invented verification/source claims
- no Android UI expansion until the mobile API and Android contract actually expose the needed fields

## Android Current Data Flow

### Programs

- Endpoint called: `GET /api/mobile/programs`
- Android DTO: `ProgramDto`
- Room entity: `ProgramEntity`
- Domain model: `Program`
- Refresh path: `ProgramsViewModel -> DataRepository.refreshPrograms() -> DegreeWikiApiService.getPrograms()`
- Detail path: `ProgramDetailViewModel -> DataRepository.getProgramById(id)` from Room only

### Universities

- Endpoint called: `GET /api/mobile/universities`
- Android DTO: `UniversityDto`
- Room entity: `UniversityEntity`
- Domain model: `University`
- Refresh path: `UniversitiesViewModel -> DataRepository.refreshUniversities() -> DegreeWikiApiService.getUniversities()`
- Detail path: `UniversityDetailViewModel -> DataRepository.getUniversityById(id)` from Room only
- Extra derived context: cached countries are used to resolve `countryId` to country name; cached programs are used to show related program titles

### Countries / Destinations

- Endpoint called: `GET /api/mobile/countries`
- Android DTO: `CountryDto`
- Room entity: `CountryEntity`
- Domain model: `Country`
- Refresh path: `CountriesViewModel -> DataRepository.refreshCountries() -> DegreeWikiApiService.getCountries()`
- Detail path: `CountryDetailViewModel -> DataRepository.getCountryById(id)` from Room only
- Extra derived context: cached universities and cached programs are joined by country id/name

## Current Endpoints Used

Verified public Android usage:

- `/api/mobile/programs`
- `/api/mobile/universities`
- `/api/mobile/countries`

Declared in Android but not verified in the current public UI:

- `/api/mobile/bootstrap`
- `/api/mobile/programs/search`

Declared in Android auth/profile flows, not part of current public browse:

- `/api/mobile/me`
- `/api/mobile/me/saved-items`

Checked web repo notes:

- Verified route files exist for `programs.ts`, `universities.ts`, and `countries.ts`
- Matching checked route files were not found for `bootstrap`, `programs/search`, `me`, or `me/saved-items`

## Web Public Page And API Findings

### Programs

Web list currently shows or derives:

- title
- university name and link
- university logo or monogram
- abbreviated degree level
- subject/field
- country ISO code
- city + country location text
- duration
- language
- intake
- deadline and urgency cue
- scholarship cue
- verification label
- source checked date
- tuition display and period
- saved state and compare state for authenticated/public interactions

Web detail currently uses:

- all core identity fields
- degree award / degree level
- study mode
- delivery mode
- language of instruction
- tuition range, period, notes
- application fee, currency, notes
- application URL
- official URL
- admission requirements
- English requirements
- GPA requirements
- curriculum summary
- career outcomes
- intake records with deadlines/status
- verification status
- last verified date
- source confidence score
- updated date
- media

Mobile API currently returns only:

- `id`
- `slug`
- `title`
- `universityName`
- `countryName`
- `degreeLevel`
- `subject`
- `tuition`
- `duration`

### Universities

Web list currently shows or derives:

- name
- logo
- city + country
- overview or ranking teaser
- program count
- verification label
- source checked date
- official site link

Web detail currently uses:

- id / slug / name
- short name
- native name
- official URL
- country and city
- institution type / ownership type
- founded year / student count
- ranking summary and ranking source URL
- overview
- campus summary
- admission overview
- application overview
- application portal URL
- international admissions URL
- admission email
- language requirement overview
- scholarship overview
- housing overview
- student life overview
- international student overview
- career support overview
- verification status
- last verified date
- source confidence score
- updated date
- logo / cover / OG media
- related programs

Mobile API currently returns only:

- `id`
- `slug`
- `name`
- `countryId`
- `city`
- `logoUrl`
- `overview`

### Destinations / Countries

Web list currently shows or derives:

- name
- cover image
- program count
- scholarship count

Web detail currently uses:

- id / slug / name
- iso2 / iso3 / continent
- overview
- verification status
- capital city
- currency code / currency name
- official languages
- common study languages
- popular student cities
- tuition overview
- living cost overview
- admission overview
- visa overview
- student work rights overview
- post-study work overview
- scholarship overview
- university system overview
- required documents overview
- intake overview
- tuition min/max annual
- living-cost min/max monthly
- student work flags/text
- post-study work flags/text
- official education URL
- official visa URL
- FAQ JSON
- source confidence score
- last verified date
- updated date
- cover / OG media
- related preview universities
- related preview programs

Mobile API currently returns only:

- `id`
- `slug`
- `name`
- `summary`
- `imageUrl`

### Scholarships

Web list currently shows or derives:

- name
- provider name
- scholarship type
- provider type
- funding type
- application type
- amount range
- currency
- deadline / deadline text
- host countries
- degree levels
- eligibility summary
- verification label
- source checked date
- logo

Web detail currently uses:

- overview
- eligibility summary
- coverage notes
- official / application / provider URLs
- linked countries
- linked universities
- linked subjects
- linked degree levels
- eligible nationalities
- verification metadata
- media
- linked programs

Verified mobile endpoint status:

- No scholarship route file found under `src/pages/api/mobile`

### Guides / Articles

Web list currently shows or derives:

- title
- category
- published date
- summary
- featured image

Web detail currently uses:

- title
- summary
- full content
- published date
- updated date
- verification metadata
- last verified date
- source confidence score
- category
- featured / OG media
- related guides

Verified mobile endpoint status:

- No guides/articles route file found under `src/pages/api/mobile`

## Program Contract Table

| Field / concept | Shown on web? | Returned by mobile API? | Present in Android DTO? | Present in Room cache? | Rendered in Android now? | Needed for mobile card? | Needed for mobile detail? | Priority | Notes |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| id | yes | yes | yes | yes | indirect | no | yes | high | Android routes/details use id lookups |
| slug | yes | yes | yes | yes | no | no | low | medium | useful for deep links later |
| title | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| university name | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| university slug/id | yes | no | no | no | no | medium | high | high | web links and related browse depend on real ids/slugs |
| country name | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| country code/id | yes | no | no | no | no | medium | medium | medium | web uses ISO/location context |
| city/location | yes | no | no | no | no | medium | high | high | web list/detail show location richness |
| degree level | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| subject/field | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| duration | yes | yes | yes | yes | yes | medium | yes | high | Android currently renders duration |
| language | yes | no | no | no | no | medium | high | high | web list/detail both use it |
| study mode | yes | no | no | no | no | low | high | medium | detail-first field |
| delivery mode | yes | no | no | no | no | low | high | medium | detail-first field |
| tuition amount/text | yes | partial | partial | partial | yes | yes | yes | high | Android has one `Double` only, not display-safe full tuple |
| currency | yes | no | no | no | no | high | high | high | required for honest mobile tuition display |
| application fee | yes | no | no | no | no | no | medium | medium | detail field |
| intake/start date | yes | no | no | no | no | medium | high | high | web detail uses intake rows |
| deadline/deadline text | yes | no | no | no | no | medium | high | high | do not fake from web cues |
| admission requirements | yes | no | no | no | no | no | high | medium | detail field |
| English requirements | yes | no | no | no | no | no | high | medium | detail field |
| GPA/background requirements | yes | no | no | no | no | no | medium | medium | detail field |
| documents | unknown | unknown | no | no | no | no | low | low | not verified in checked program detail file |
| curriculum/modules | yes | no | no | no | no | no | medium | medium | web has `curriculum_summary` |
| career outcomes | yes | no | no | no | no | no | medium | medium | web has `career_outcomes` |
| scholarships/funding cue | yes | no | no | no | no | medium | medium | medium | web card has scholarship badge |
| official URL | yes | no | no | no | no | no | high | high | strong detail value |
| verification status | yes | no | no | no | no | medium | high | high | web shows trust labels |
| last verified/source checked date | yes | no | no | no | no | medium | high | high | should come from API, not inferred |
| source confidence | yes | no | no | no | no | no | medium | medium | detail/trust field |
| image/media URL | yes | no | no | no | no | medium | medium | medium | logos/hero media absent in Android program contract |
| saved state if authenticated | yes | no | no | no | no | low | low | low | probably separate auth-specific contract |

## University Contract Table

| Field / concept | Shown on web? | Returned by mobile API? | Present in Android DTO? | Present in Room cache? | Rendered in Android now? | Needed for mobile card? | Needed for mobile detail? | Priority | Notes |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| id | yes | yes | yes | yes | indirect | no | yes | high | Android detail lookup key |
| slug | yes | yes | yes | yes | no | no | low | medium | useful later for deep links |
| name | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| short/native name | yes | no | no | no | no | low | medium | low | detail enhancement |
| country | yes | partial | partial | partial | yes | yes | yes | high | Android gets only `countryId`, resolves name from country cache |
| city | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| overview | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| official URL | yes | no | no | no | no | medium | high | high | visible web list/detail gap |
| admissions URL | yes | no | no | no | no | no | high | medium | includes portal URL |
| international admissions URL | yes | no | no | no | no | no | medium | medium | detail field |
| logo/image/media | yes | partial | partial | partial | no | medium | medium | medium | Android receives logo URL only and does not render it |
| verification status | yes | no | no | no | no | medium | high | high | web trust signals absent |
| last verified date | yes | no | no | no | no | medium | high | high | trust field |
| ranking/ranking summary | yes | no | no | no | no | medium | medium | medium | web list/detail use ranking summary |
| campus summary | yes | no | no | no | no | no | medium | medium | detail field |
| student support | yes | no | no | no | no | no | medium | medium | web has student/international support summaries |
| housing | yes | no | no | no | no | no | medium | medium | detail field |
| scholarships summary | yes | no | no | no | no | no | medium | medium | detail field |
| language requirements | yes | no | no | no | no | no | medium | medium | detail field |
| career support | yes | no | no | no | no | no | medium | medium | detail field |
| related programs | yes | partial | partial | partial | yes | low | medium | low | Android derives from cached program names only |
| source metadata | yes | no | no | no | no | no | medium | medium | source confidence absent |

## Country / Destination Contract Table

| Field / concept | Shown on web? | Returned by mobile API? | Present in Android DTO? | Present in Room cache? | Rendered in Android now? | Needed for mobile card? | Needed for mobile detail? | Priority | Notes |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| id | yes | yes | yes | yes | indirect | no | yes | high | Android detail lookup key |
| slug | yes | yes | yes | yes | no | no | low | medium | future deep links |
| name | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| iso2/code | yes | no | no | no | no | low | medium | medium | web detail uses ISO facts |
| overview / summary | yes | yes | yes | yes | yes | yes | yes | high | present end to end |
| destination summary | yes | yes | yes | yes | yes | yes | yes | high | Android `summary` maps from web `overview` |
| currency | yes | no | no | no | no | low | medium | medium | code/name missing |
| capital/city facts | yes | no | no | no | no | no | medium | low | detail enhancement |
| tuition overview | yes | no | no | no | no | no | high | high | high-value study-abroad field |
| living cost overview | yes | no | no | no | no | no | high | high | high-value detail field |
| visa/work guidance | yes | no | no | no | no | no | high | high | includes visa and student work rights |
| post-study work summary | yes | no | no | no | no | no | high | high | high-value detail field |
| scholarship summary | yes | no | no | no | no | no | medium | medium | detail field |
| official education URL | yes | no | no | no | no | no | medium | medium | trustworthy outbound link |
| official visa URL | yes | no | no | no | no | no | medium | medium | trustworthy outbound link |
| FAQ entries | yes | no | no | no | no | no | medium | medium | web uses `faq_json` |
| related universities | yes | partial | partial | partial | yes | low | medium | low | Android derives names only from cache |
| related programs | yes | partial | partial | partial | yes | low | medium | low | Android derives names only from cache |
| media/image | yes | yes | yes | yes | no | medium | low | low | Android stores image URL but does not render it |
| verification status | yes | no | no | no | no | low | high | medium | web detail trust note absent |
| last verified date | yes | no | no | no | no | low | high | medium | trust field |

## Scholarships Readiness

Current readiness:

- Public web list and detail pages exist
- No verified `src/pages/api/mobile` scholarship endpoint exists
- Android has no scholarship DTO/entity/domain/UI flow

Needed future mobile list fields:

- id
- slug
- name
- provider name
- scholarship type
- funding type
- application type
- amount text or structured amount + currency
- deadline text/date
- host countries
- degree levels
- short eligibility summary
- verification status
- last verified date
- logo/image URL

Needed future mobile detail fields:

- overview
- eligibility summary
- coverage notes
- official URL
- application URL
- provider URL
- linked universities
- linked programs
- linked countries
- linked subjects
- eligible nationalities
- verification status
- last verified date
- source confidence score

Likely login-sensitive actions:

- save scholarship if product later allows it
- any personalized shortlist workflow

Should be deferred:

- compare flows
- application tracking
- anything requiring complex relationship joins until core public browse exists

## Guide / Article Readiness

Current readiness:

- Public web list and detail pages exist
- No verified `src/pages/api/mobile` guide/article endpoint exists
- Android has no guide DTO/entity/domain/UI flow

Needed future mobile list fields:

- id
- slug
- title
- summary
- category name/id
- published date
- featured image URL

Needed future mobile detail fields:

- title
- summary
- content or mobile-safe structured article body
- category
- published date
- updated date
- verification status
- last verified date
- source confidence score
- featured image URL
- related guides

Likely login-sensitive actions:

- none for basic public reading

Should be deferred:

- offline article-body caching strategy if content is large
- personalization, save, or recommendation logic

## Missing Mobile API Fields

Highest-value missing fields across current public entities:

- Program:
  - universityId or universitySlug
  - countryId or ISO code
  - city
  - language
  - studyMode
  - deliveryMode
  - tuition currency and period
  - official/application URLs
  - verification status
  - last verified date
  - intakes/deadlines
  - structured requirements
- University:
  - official URL
  - short/native name
  - verification status
  - last verified date
  - ranking summary
  - admissions links
  - language/scholarship/housing/support summaries
- Destination:
  - iso2
  - capital/currency/language facts
  - tuition overview and range
  - living cost overview and range
  - visa/work/post-study work guidance
  - official URLs
  - FAQ
  - verification metadata

## Recommended Backend / Mobile API Improvements

1. Keep collection endpoints lightweight but honest.

2. Expand current list payloads only for fields that clearly improve cards now:

- program language
- program city/location
- program tuition currency/period
- program verification status
- university country name
- university official URL only if card/detail plans are immediate
- country iso2 and maybe a short trust field only if needed

3. Add dedicated public detail-capable mobile endpoints or expanded detail payloads for:

- programs
- universities
- countries/destinations

4. Keep trust metadata structured, not preformatted:

- `verificationStatus`
- `lastVerifiedAt`
- `sourceConfidenceScore`

5. Keep money/date fields structured where possible, with optional companion display text only if formatting consistency is required across clients.

6. Add scholarships and guides as separate mobile surfaces with dedicated endpoints, not as opportunistic additions to current entity routes.

## Android Implementation Recommendations

1. Do not add UI fields until the matching mobile API field exists and is persisted.

2. When the API expands, update in order:

- DTO
- mapper
- Room entity
- domain model
- repository consumers
- UI rendering

3. Prefer structured detail sections over placeholder-heavy screens.

4. Add program detail data first, because that is the largest visible web/mobile gap.

5. Add university and destination trust metadata early so Android can show official, source-aware context without guessing.

6. Treat scholarships and guides as separate bundles after core entity detail parity improves.

## Strict No-Fake-Data Rule

Android must not:

- synthesize tuition text from incomplete numeric data
- guess deadlines from generic intake wording
- infer verification badges without explicit API fields
- hardcode scholarship availability
- invent visa/work guidance from web copy not present in the mobile contract
- show source-check dates unless they are explicitly returned by the API
