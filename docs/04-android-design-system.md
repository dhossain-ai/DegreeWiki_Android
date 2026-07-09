# Android Design System

Last audited: 2026-07-09

## Current Implementation

The app uses Compose Material 3 theming with a custom `DegreeWikiTheme`.

## Verified Theme Tokens

### Light Theme Direction

- Primary: deep academic navy blue
- Secondary: teal
- Tertiary: indigo
- Background: very light slate/off-white
- Surface: white
- Text: dark slate/navy

This is directionally compatible with the intended DegreeWiki product identity, especially for the light theme.

### Dark Theme

- Full dark palette exists and follows the same token families
- Product guidance provided for this audit emphasized a warm light educational feel; dark mode exists in code but has not been separately product-reviewed here

## Typography

- Uses Material 3 typography slots
- All styles currently use `FontFamily.SansSerif`
- No custom academic type family is bundled

## Current Component Patterns

- `Scaffold` + `TopAppBar`
- `NavigationBar`
- `ElevatedCard`
- `AssistChip`
- `OutlinedTextField`
- `Button` and `OutlinedButton`
- `CircularProgressIndicator`
- `LinearProgressIndicator`

## Current UX Character

Verified current feel:

- clean
- simple
- list-first
- generic Material 3

Not yet strongly expressed:

- mature education portal tone
- structured academic information density
- verified/trust status language
- deadline/status color semantics
- source-aware fact presentation

## Loading, Error, Empty States

Verified patterns:

- list screens show centered spinner on load
- list screens show generic retry state on error
- list screens show simple empty text plus refresh button
- detail screens show spinner, content, or plain "not found" text
- profile shows card header, spinner, empty state, and inline removal actions

Current limitations:

- states are functional but visually minimal
- no shared state components
- no trust-specific empty or error copy
- some repository failures may not surface as UI error states because refresh exceptions are swallowed

## Placeholder Or Non-Product-Safe UI

- `DiscoverScreen` literal placeholder text
- `ChatScreen` literal placeholder text
- `FitFinderScreen` literal placeholder text
- Auth screen includes a visible "Continue with Google" button that is explicitly a placeholder TODO

These should not be documented as real user-ready flows.

## Design Guidance For Future Work

- Preserve the light academic palette direction already present in the light theme
- Increase structured content presentation before adding visual novelty
- Prefer source/trust/status components tied to real API data rather than cosmetic badges
- Replace placeholder CTAs before expanding navigation to unfinished features
