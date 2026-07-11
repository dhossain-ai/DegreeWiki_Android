# Android Product Flow Map

Last updated: 2026-07-11

## A. Purpose

This file maps the current DegreeWiki Android public flow after the Bundle 5 contract audit.

## B. Public Access Without Login

Public users can currently access:

- Home
- Programs list
- Program detail
- Universities list
- University detail
- Destinations list
- Country detail

## C. Current Public Data Flow

Public browse is currently a collection-cache-detail loop, not a list-plus-detail-endpoint model.

Flow:

1. Screen ViewModel triggers `DataRepository.refreshPrograms()`, `refreshUniversities()`, or `refreshCountries()`.
2. Repository calls one thin collection mobile endpoint:
   - `/api/mobile/programs`
   - `/api/mobile/universities`
   - `/api/mobile/countries`
3. DTOs are mapped directly into Room entities.
4. Lists render from Room-backed flows.
5. Detail screens load by cached `id` lookup only.
6. University and country details derive a little extra context by joining other cached collections.

Implications:

- No Android public detail endpoint is currently called.
- Rich web-only detail fields cannot appear on Android until the mobile API and Android DTO/cache contract expand.
- Android correctly omits missing fields instead of fabricating them.

## D. Bottom Navigation

Current bottom navigation:

- Home
- Programs
- Universities
- Countries
- Profile

## E. Detail Screen Behavior

Current behavior:

- Program detail shows only cached program fields: title, university, country, degree level, subject, duration, tuition.
- University detail shows cached university fields plus resolved country name and related cached program titles.
- Country detail shows cached summary plus related cached universities and programs.
- Missing fields are omitted instead of replaced with fake placeholders.
- Unavailable records show a safe fallback state with back navigation.

## F. Web-To-Android Gap

The public web app already shows richer information than Android can currently render.

Not yet available in the Android public contract:

- program language, study mode, delivery mode, city, official links, deadlines, requirements, curriculum, career outcomes, verification metadata
- university official links, ranking, admissions links, support/housing/scholarship/language/career sections, verification metadata
- destination ISO/currency/capital facts, tuition and living-cost guidance, visa/work guidance, official URLs, FAQ, verification metadata

## G. Deferred Public Surfaces

These still remain future Android work:

- Scholarships
- Guides and articles
- Search/filter UX built on live public endpoints
- Fit Finder
- Chat

## H. Data Truth Rule

Android must show only API-backed or cache-backed data.

Do not invent:

- tuition
- deadlines
- verification labels
- source-checked dates
- visa guidance
- scholarship availability
- guide content
- related content not backed by current cache

## I. Next Flow Recommendation

The next safe product step is to keep the current flow shape but expand the mobile contract deliberately:

1. add structured public detail fields to the mobile API
2. update Android DTOs/entities/mappers
3. render only newly verified fields
4. add scholarships/guides only after dedicated mobile-safe endpoints exist
# Bundle 7 detail loading behavior

Program, university, and country card taps still navigate by cached Room id. Each detail ViewModel observes the cached entity, uses its slug to request the matching public detail endpoint, and merges the in-memory result into the screen. Missing slug, HTTP failure, malformed/partial payload, or 404 leaves the cached record visible. If no cached record exists, the existing friendly unavailable state remains.
