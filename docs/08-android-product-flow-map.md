# Android Product Flow Map

Last updated: 2026-07-12 (Bundle 9)

## Purpose And Design Source

This document maps DegreeWiki Android’s current and planned student flows. Bundle 8’s target screen hierarchy, language, and interaction rules are defined in `docs/10-android-mobile-ux-blueprint.md`; that blueprint is the design source for the implementation bundles that follow.

Desktop web may inform product logic and visual identity, but Android remains a mobile-native product and must not copy desktop layouts directly.

## Public Access Without Login

Public users can currently access:

- Home
- Programs list and program detail
- Universities list and university detail
- Countries list and country detail
- Profile entry and login

Public discovery must not be blocked by login. Missing data is omitted, and all displayed facts must be backed by API or cache data.

## Current Public Data Flow

Public browse uses cache-backed collection screens plus richer in-memory detail responses:

1. A list ViewModel refreshes programs, universities, or countries from the corresponding mobile collection endpoint.
2. Collection DTOs are mapped into Room and list screens observe Room-backed flows.
3. A card tap navigates to detail by the cached record ID.
4. The detail ViewModel renders the cached record first, resolves its slug, and requests the matching public detail endpoint.
5. A successful detail response enriches the screen in ViewModel memory; rich detail fields are not persisted to Room.
6. A missing slug, HTTP failure, malformed/partial response, or 404 leaves the cached record visible.
7. If no cached record exists, the existing friendly unavailable state is shown.

This fallback behavior keeps public detail navigation reliable without inventing content.

## Near-Term Navigation

Bottom navigation remains:

- Home
- Programs
- Universities
- Countries
- Profile

Navigation rules:

- Do not add Chat as a bottom tab.
- Do not add Fit Finder as a bottom tab yet.
- Introduce Scholarships and Guides through Home or Explore entry cards before considering any bottom-navigation change.
- Reconsider future tabs only after search, saved items, and Fit Finder have mature flows.

## Target Discovery Flow

```text
Home
  -> Search card -> Programs list -> Program detail
  -> Programs browse card -> Programs list
  -> Universities browse card -> Universities list -> University detail
  -> Countries browse card -> Countries list -> Country detail
  -> Fit Finder CTA -> deferred until Bundle 15
  -> Scholarships / Guides cards -> deferred until public API and Android screens exist
```

Featured Home content should appear only when real data is available. Unsupported chips, searches, filters, or entry cards must remain documented guidance rather than fake interactive controls.

## Bundle 9 Implemented Discovery Flow

- Home remains the first tab.
- The Home search entry routes to Programs; it does not claim advanced or cross-entity search.
- Programs, Universities, and Countries browse cards switch to their existing bottom-navigation destinations.
- Featured programs, popular universities, and study destinations each show at most three real records in horizontal rows and route to the matching browse screen.
- The Fit Finder card is visibly deferred and has no working action.
- Scholarships, Guides, and Chat are not exposed as working destinations.
- Program, university, and destination list cards preserve their existing ID-based detail navigation.
- Offline content, refresh warnings, empty states, and retry behavior remain unchanged in flow.
- Bottom navigation keeps `Countries`; the destination screen uses the more student-facing heading `Study destinations`.

## Detail Flow

- Program detail targets a hero, action row, key facts, structured content, source/verification, and related programs.
- University detail targets a hero, official website action, overview, facts, admissions/support sections, programs, and source/verification.
- Country detail targets a destination overview, quick facts, costs, visa/work guidance, education/admissions, related universities/programs, and official sources.
- Each fact and section is conditional on real data.
- Official links should become explicit actions in Bundle 10.

## Login-Gated And Account Flows

Login is required only where account persistence or protected backend behavior requires it.

- Public browsing and detail reading: no login.
- Profile benefits and login entry: public.
- Saving programs and cross-device saved items: login-gated when implemented.
- Persistent Fit Finder results: login-gated when implemented.
- Fit Finder entry may be visible publicly, but must not imply the flow works before Bundle 15.
- Whether an anonymous Fit Finder trial is possible remains an open backend/product decision.
- Compare may be public and local or account-backed; this remains an open decision.

Profile should explain student benefits before or alongside the supported email/password form. Do not expose fake Google login or any unsupported authentication method.

## Deferred Public Surfaces

- Scholarships and Guides: Home/Explore entry points first; public list/detail screens after verified APIs exist.
- Fit Finder: Home CTA and later profile/dashboard placement; backend-scored real programs only.
- Chat: small contextual help placement only; not bottom navigation and not AI-first positioning.
- Search/filter: Programs routing is acceptable near-term; real search inputs, chips, filters, and sorting belong to Bundle 11.

## Data And Trust Rules

- Show only real API-backed or cache-backed data.
- Omit missing facts and empty sections.
- Never invent tuition, deadlines, verification, source dates, visa/work rules, scholarships, guide content, requirements, or related records.
- Do not expose implementation terms such as cache, API sync, endpoint, or internal source signals to students.
- Use a compact independence reminder and ask students to confirm final details on official university or scholarship pages.

## Planned Bundle Sequence

1. Bundle 9 — Home + Public List Redesign (implemented)
2. Bundle 10 — Detail Screen Redesign
3. Bundle 11 — Search + Filter UX
4. Bundle 12 — Scholarships/Guides API in the web repo
5. Bundle 13 — Scholarships/Guides Android
6. Bundle 14 — Profile/Saved Items
7. Bundle 15 — Fit Finder
8. Bundle 16 — contextual Chat
