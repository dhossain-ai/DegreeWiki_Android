# Android Product Decisions

Last audited: 2026-07-09

## Scope Decisions Verified In Repo

- Native Android app, not WebView-based
- Data-backed content only
- Public discovery focuses on programs, universities, and countries
- Authentication uses Supabase email/password
- Profile area is auth-gated inside the main tab shell
- Saved items are read from a protected mobile endpoint and cached locally

## Product Decisions Already Reflected In Code

- Keep the app lightweight and list-first rather than feature-heavy
- Use server data as the source of truth
- Cache fetched content locally with Room for local reuse
- Attach bearer auth to protected API requests using the active Supabase session

## Explicitly Not Verified In This Repo

The following appear in older planning material but are not verified as working Android features here:

- Native Fit Finder flow
- Native AI chat flow
- Scholarship browsing
- Search/filter UI over public content
- Recently viewed content
- Offline sync policy beyond simple Room persistence
- Push notifications
- Student profile/preferences beyond basic account display
- Source metadata rendering for programs, universities, or countries
- Verified badges, deadline badges, or academic trust UI patterns

## Current Product Risks

- Placeholder screens for chat and fit finder exist in source and should not be treated as shipped product.
- The app currently exposes only basic list/detail browsing and profile saved items.
- Detail pages show only fields already present in cached list payloads, so trust-rich detail content is limited by the current contract.

## Decisions For Near-Term Work

- Do not expand product claims in docs beyond the verified Android surface.
- Keep Android work aligned to existing `/api/mobile/*` endpoints unless backend changes are separately approved.
- Treat the Android app as a browse-first client until detail/search/save flows are fully verified end to end.

## Documentation Rule Going Forward

If a feature is not wired in navigation, backed by repo code, and supported by a verified API contract, document it as deferred or unverified rather than present.
