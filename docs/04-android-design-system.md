# Android Design System

Last updated: 2026-07-12

The target Android experience is defined in `docs/10-android-mobile-ux-blueprint.md`. This document summarizes the reusable visual, content, and data-display rules that implementation bundles should follow.

## Product Direction

- Design for a mobile-native, student-first study-abroad search experience.
- Use desktop web only as a reference for product logic and visual identity; do not copy desktop layouts into Android.
- Favor search-first discovery, calm hierarchy, readable one-column layouts, and short scannable cards.
- Keep the product source-aware and trustworthy without overclaiming verification or completeness.
- Do not position DegreeWiki as an AI dashboard, agency marketing app, or database/admin viewer.
- Every rendered fact must come from real API or cache data.

## Color And Surface Direction

- Page canvas: warm off-white.
- Cards: white with subtle borders or restrained shadows.
- Headings: academic navy.
- Secondary text: slate.
- Primary actions: academic blue.
- Verified state: green only when backed by a real public verification field.
- Deadline or time-sensitive state: amber only when backed by real deadline data.
- Deferred or unavailable states: neutral gray.

Use generous spacing, consistent rounded card corners, and clear section separation. Avoid crowded desktop-style grids. Horizontal carousels are appropriate for compact featured content; future filter controls should use a mobile bottom sheet.

## Copy Rules

### Use

- “Find degrees abroad”
- “Search programs by subject, country, or university”
- “Compare tuition, duration, language, and deadlines”
- “Explore universities”
- “Plan where to study”
- “Save programs after signing in”
- “Confirm final details on official sources”

### Avoid

- “real browse data”
- “synced from the API”
- “cached programs”
- “current mobile catalog”
- “cached detail screen”
- “fields available in cache”
- “AI dashboard”
- “source signals”
- “generated recommendations”
- TODO, placeholder, implementation, cache, sync, endpoint, or developer-facing text in student UI

Student copy should describe a benefit, action, or honest product state. Technical implementation language belongs only in developer documentation and diagnostics.

## Screen And Card Rules

- Prefer a warm off-white screen background, white cards, and a clear hero or page heading.
- Keep top-level layouts vertically scrollable and single-column.
- Use shared loading, empty, error, and refresh-warning patterns.
- Keep trust notes compact and place them near relevant content or at the end of a screen.
- Avoid presenting unfinished features as live product capabilities.

### List Cards

- Put the record name first, supporting identity/location second, then a small facts or badges row.
- Program and university titles should normally use no more than two lines.
- Overview, description, or destination teaser text must use no more than two lines.
- Show only the two or three highest-value facts that support scanning and comparison.
- Do not turn a list card into a miniature detail page or show full paragraphs.
- Never show raw IDs, `null`, empty labels, fake defaults, or repeated “not available” values.

### Detail Sections

- Start with a hero/header, then actions and key facts when supported.
- Group long information under descriptive headings instead of using uninterrupted text blocks.
- Use structured sections in the order recommended by the mobile UX blueprint for programs, universities, and countries.
- Render related records as compact cards.
- Official URLs should become clear actions when implemented, not raw text or unverified claims.
- Verification badges and source notes must reflect real public fields and use cautious wording.

## Omit Missing Data

- Omit an individual fact when its value is missing.
- Omit an entire section when it has no real content.
- Do not replace missing values with guessed values, fake deadlines, fake sources, placeholder facts, raw IDs, `null`, or repeated “not available” text.
- Do not infer formal verification from the presence of a URL or API record.
- Empty states may explain what the student can do next, but must not fabricate content.

## Current Reusable Components

- `DegreeWikiScreen`
- `DegreeWikiCard`
- `SectionHeader`
- `TrustNote`
- `RefreshWarningNote`
- `StatusBadge`
- `PrimaryActionButton`
- `EmptyState`
- `ErrorState`
- `LoadingState`
- `ScreenHero`
- `QuickBrowseCard`
- `DeferredFeatureCard`
- `SearchEntryCard`

These live in `app/src/main/java/com/example/degreewiki/ui/components/DegreeWikiComponents.kt`. Bundle 8 changes their design guidance only; it does not modify implementation.

## Current Gaps

- Existing screens and components do not yet fully meet the blueprint’s hierarchy and copy standards.
- List cards still need compact text treatment and stronger comparison facts.
- Official links are not yet consistently actionable.
- Bottom navigation uses `Countries` to avoid truncation; `Destinations` remains an open naming decision.
- Typography still uses the system sans-serif family.
