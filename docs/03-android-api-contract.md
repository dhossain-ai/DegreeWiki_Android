# Android API Contract

Last audited: 2026-07-09

## Rule

This file lists only endpoints and fields verified in the Android repo source. It does not speculate beyond the current client contract.

## Runtime Base URL

- Base URL comes from `BuildConfig.API_BASE_URL`
- Value is injected from `local.properties`
- Exact environment value was not documented here on purpose

## Verified Endpoints In Use

### Public Or Optional Auth

`GET /api/mobile/bootstrap`

- Response model:
  - `ok: Boolean`
  - `siteName: String`
  - `featureFlags: Map<String, Boolean>`
- Verified in API interface
- Not verified as used by any current screen/view model

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
- Verified in API interface
- Not verified as used by any current screen/view model

`GET /api/mobile/programs`

- Response model:
  list of `ProgramDto`

`GET /api/mobile/countries`

- Response model:
  list of `CountryDto`

`GET /api/mobile/universities`

- Response model:
  list of `UniversityDto`

### Auth Required

`GET /api/mobile/me`

- Response model:
  - `id: String`
  - `email: String`
  - `name: String`
  - `role: String`

`GET /api/mobile/me/saved-items`

- Response model:
  list of `SavedItemDto`

`DELETE /api/mobile/me/saved-items/{id}`

- Path param:
  - `id: String`
- Response body:
  not modeled in client

## Verified DTOs

### ProgramDto

- `id: String`
- `slug: String`
- `title: String`
- `universityName: String`
- `countryName: String`
- `degreeLevel: String`
- `subject: String?`
- `tuition: Double?`
- `duration: String?`

### UniversityDto

- `id: String`
- `slug: String`
- `name: String`
- `countryId: String`
- `city: String?`
- `logoUrl: String?`
- `overview: String?`

### CountryDto

- `id: String`
- `slug: String`
- `name: String`
- `summary: String?`
- `imageUrl: String?`

### UserProfileDto

- `id: String`
- `email: String`
- `name: String`
- `role: String`

### SavedItemDto

- `id: String`
- `entityType: String`
- `entityId: String`
- `title: String`
- `slug: String`
- `thumbnail: String?`
- `savedAt: Long`

## Auth Contract

- Supabase auth session determines logged-in state
- Protected API calls use `Authorization: Bearer <access_token>` when a session exists
- Auth token injection is automatic via `AuthInterceptor`

## Verified Client Gaps

- No Android client call for `POST /api/mobile/me/saved-items`
- No Android client call for program detail by slug or id
- No Android client call for university detail by slug or id
- No Android client call for country detail by slug or id
- No Android client call for chat or fit finder endpoints

## Contract Mismatch Risks

- `UniversityDto` exposes `countryId`, but the detail UI currently displays that raw value directly rather than a resolved country name.
- Search/bootstrap DTOs exist without active UI integration, so they should be treated as available contract surface, not shipped user flows.
- Because `Json { ignoreUnknownKeys = true }` is enabled, additive backend fields will be ignored silently by the current client.
