# Android Product Flow Map

Last updated: 2026-07-14 (Bundle 13)

## Purpose And Design Source

This document maps DegreeWiki Android’s current and planned student flows. Bundle 8’s target screen hierarchy, language, and interaction rules are defined in `docs/10-android-mobile-ux-blueprint.md`; that blueprint is the design source for the implementation bundles that follow.

Desktop web may inform product logic and visual identity, but Android remains a mobile-native product and must not copy desktop layouts directly.

## Public Access Without Login

Public users can currently access:

- Home
- Programs list and program detail
- Universities list and university detail
- Countries list and country detail
- Scholarships list and scholarship detail, entered from Home
- Study guides list and guide detail, entered from Home
- Profile entry and login

Public discovery must not be blocked by login. Missing data is omitted, and all displayed facts must be backed by API or cache data.

## Current Public Data Flow

Public browse uses cache-backed collection screens plus richer in-memory detail responses:

1. A list ViewModel refreshes programs, universities, countries, scholarships, or guides from the corresponding mobile collection endpoint.
2. Collection DTOs are mapped into Room and list screens observe Room-backed flows.
3. Program/University/Country cards navigate by cached ID; Scholarship/Guide cards navigate by real slug.
4. The detail ViewModel renders the cached record first and requests the matching public detail endpoint.
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
  -> Scholarships card -> Scholarships list -> Scholarship detail
  -> Guides card -> Study guides list -> Guide detail -> Related guide detail
```

Featured Home content should appear only when real data is available. Unsupported chips, searches, filters, or entry cards must remain documented guidance rather than fake interactive controls.

## Bundle 9 Implemented Discovery Flow

- Home remains the first tab.
- The Home search entry routes to Programs; it does not claim advanced or cross-entity search.
- Programs, Universities, and Countries browse cards switch to their existing bottom-navigation destinations.
- Featured programs, popular universities, and study destinations each show at most three real records in horizontal rows and route to the matching browse screen.
- The Fit Finder card is visibly deferred and has no working action.
- Scholarships and Guides are active Home destinations; Chat remains unavailable.

## Bundle 13 Implemented Public Content Flow

- Home has compact active Scholarship and Guide cards without adding a bottom tab or Explore tab.
- Scholarship and Guide lists render Room first, refresh from raw-array endpoints, retain cached
  content with a warning after refresh failure, and provide friendly loading/error/empty/retry states.
- Detail routes use slugs and wrapped `{ ok, item }` endpoints. Rich detail stays in memory while a
  cached summary remains as failure fallback.
- Scholarship dates are parsed defensively and never imply open/available/closing-soon state.
- Guide articles render documented JSON blocks as native Compose; unsafe links are non-clickable,
  unknown blocks are skipped, and related guide slugs navigate normally.
- Bottom navigation remains Home, Programs, Universities, Countries, Profile.
- Program, university, and destination list cards preserve their existing ID-based detail navigation.
- Offline content, refresh warnings, empty states, and retry behavior remain unchanged in flow.
- Bottom navigation keeps `Countries`; the destination screen uses the more student-facing heading `Study destinations`.

## Detail Flow

- Program detail targets a hero, action row, key facts, structured content, source/verification, and related programs.
- University detail targets a hero, official website action, overview, facts, admissions/support sections, programs, and source/verification.
- Country detail targets a destination overview, quick facts, costs, visa/work guidance, education/admissions, related universities/programs, and official sources.
- Each fact and section is conditional on real data.
- Official links are explicit external actions only when their URL uses `http` or `https`.

## Bundle 10 Implemented Detail Flow

- Toolbars use `Program details`, `University`, and `Study destination`; entity names remain in the hero.
- Program detail presents hero, official actions, key facts, decision sections, expandable curriculum/careers, and compact source status.
- University detail presents hero, website action, facts, one expandable About section, conditional support sections, tappable related programs, and source status.
- Country detail presents hero, facts, planning content before narrative, tappable related universities/programs, expandable About content, FAQ accordions, official-source actions, and source status.
- Related records navigate only when the existing cached object provides a real ID.
- System/back-arrow navigation replaces redundant bottom back buttons.
- Missing or unsafe URLs produce no action; malformed opening attempts are failure-safe.
- Cache-first fallback and rich in-memory detail loading remain unchanged.

## Bundle 11 Implemented Search Flow

- Home accepts a real program query and sends it once to Programs; blank submission safely opens Programs.
- Programs searches the already loaded Room-backed collection locally and never calls a search endpoint.
- Query, active filters, and sort live in the Programs ViewModel, so detail navigation and recomposition preserve discovery state.
- Program filters support degree level, country, subject, and a bounded university section using values derived from real records.
- Filter-sheet edits remain drafts until Apply; dismissing the sheet does not change active filters.
- Universities search locally by name, city, and overview. Study destinations search locally by country name and summary.
- Search results retain existing detail navigation, refresh warnings, offline visibility, and error behavior.

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
- Search/filter: local browse search, program filters, chips, and sorting are implemented; remote autocomplete remains deferred.

## Data And Trust Rules

- Show only real API-backed or cache-backed data.
- Omit missing facts and empty sections.
- Never invent tuition, deadlines, verification, source dates, visa/work rules, scholarships, guide content, requirements, or related records.
- Do not expose implementation terms such as cache, API sync, endpoint, or internal source signals to students.
- Use a compact independence reminder and ask students to confirm final details on official university or scholarship pages.

## Planned Bundle Sequence

1. Bundle 9 — Home + Public List Redesign (implemented)
2. Bundle 10 — Detail Screen Redesign (implemented)
3. Bundle 11 — Search + Filter UX (implemented)
4. Bundle 12 — Scholarships/Guides API in the web repo
5. Bundle 13 — Scholarships/Guides Android
6. Bundle 14 — Profile/Saved Items
7. Bundle 15 — Fit Finder
8. Bundle 16 — contextual Chat
