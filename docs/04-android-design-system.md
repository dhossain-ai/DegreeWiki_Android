# Android Design System

Last audited: 2026-07-10

## Current Color Direction

- Background: warm off-white for screen-level surfaces
- Cards: white with subtle elevation and soft outline treatment
- Primary actions and headings: academic navy
- Secondary copy: slate
- Trust accent: green used only for trust-note treatment, not as fake verification
- Deferred/soon states: neutral gray and amber badge tones

## Reusable Components Introduced

- `DegreeWikiScreen`
- `DegreeWikiCard`
- `SectionHeader`
- `TrustNote`
- `StatusBadge`
- `PrimaryActionButton`
- `EmptyState`
- `ErrorState`
- `LoadingState`
- `ScreenHero`
- `QuickBrowseCard`
- `DeferredFeatureCard`
- `SearchEntryCard`

These live in `app/src/main/java/com/example/degreewiki/ui/components/DegreeWikiComponents.kt` and are now used by Home plus the public browse lists.

## Mobile Card And Shell Rules

- Prefer a warm off-white app background instead of generic gray or purple-tinted surfaces.
- Keep top-level screens scrollable with a clear hero heading and short orientation copy.
- Use white cards with rounded corners, modest shadow, and enough padding for dense study-abroad data.
- Keep list cards factual and compact. Show only fields already available from API/cache-backed models.
- Use shared empty, loading, and error states instead of ad hoc centered text.
- Keep trust copy compact and source-aware.
- Avoid making unfinished surfaces feel like live product features.

## No-Fake-Data Rule

- Do not invent programs, universities, destinations, scholarships, tuition, deadlines, or source metadata.
- Do not use green badges to imply verification unless that status is truly backed by data.
- Real API/cache-backed content may be labeled neutrally, but not presented as formally verified.
- Deferred surfaces such as chat, Fit Finder, scholarships, and guides must stay clearly planned or login-gated until real Android flows exist.

## Current Gaps

- Detail screens still use older layouts and have not been fully brought onto the shared shell.
- Bottom navigation uses truncation on narrow devices to keep `Destinations` on one line.
- Typography still relies on system sans-serif rather than a bundled product type family.
