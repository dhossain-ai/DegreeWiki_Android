# DegreeWiki Android Mobile UX Blueprint

Last updated: 2026-07-12 (Bundle 9 implementation notes)

## 1. Purpose

This blueprint defines the target mobile UX, content hierarchy, and screen structure for DegreeWiki Android now that the app is technically stable and consumes richer public detail data. It is the design source for the implementation bundles that follow.

This is a mobile-native design guide. The desktop web product is a reference for product logic and visual identity only; Android must not copy desktop layouts directly. Every implementation must use real API or cache data only. The target is a trustworthy, student-first study-abroad search product: calm, premium, source-aware, and easy to scan—not an AI dashboard, agency marketing app, or database/admin viewer.

## 2. Design Principles

1. **Student-first language.** Lead with student goals, decisions, and next actions.
2. **No internal or developer copy.** Never expose cache, sync, endpoint, model, source-signal, or implementation terminology in student UI.
3. **Data-backed only.** Every program, fact, badge, recommendation, link, and related record must be backed by real API or cache data.
4. **Omit missing data.** Hide missing facts and empty sections instead of showing guessed values, fake placeholders, raw IDs, or repeated “not available” text.
5. **Search-first discovery.** Make it easy to begin with a study interest, subject, country, or university.
6. **Short, scannable cards.** List cards summarize; detail screens explain.
7. **Cautious trust cues.** Show official sources and verification states when supported, without implying editorial review, completeness, or guaranteed accuracy.
8. **Calm premium education feel.** Use restrained color, clear typography, comfortable spacing, and predictable hierarchy.
9. **No AI-first positioning.** Fit Finder and Chat are supporting tools, not the product identity.
10. **Mobile-native composition.** Prefer one-column flows, compact horizontal content, bottom sheets, and touch-friendly actions over crowded desktop-style grids.

## 3. Copy Rules

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
- TODO, placeholder, or developer text

Copy should be short, direct, and useful. Technical reliability states should become student-facing language—for example, “Showing saved information. We couldn’t refresh it right now.”

## 4. Mobile Navigation Model

The near-term bottom navigation remains:

- Home
- Programs
- Universities
- Countries
- Profile

Rules:

- Do not add Chat as a bottom tab.
- Do not add Fit Finder as a bottom tab yet.
- Introduce Scholarships and Guides as Home or Explore entry cards before considering them for bottom navigation.
- Reconsider future navigation only after search, saved items, and Fit Finder are mature enough to justify permanent destinations.

## 5. Home Screen Blueprint

Home should create a clear rhythm: identity, search, suggestions, primary browse paths, optional assistance, real featured content, and a small trust note.

### A. Top Identity Block

- Product name: “DegreeWiki”
- Primary line: “Find degrees abroad”
- Subtitle: “Search programs, universities, scholarships, and study destinations.”

Keep this block compact. It should orient the student without becoming a large marketing hero.

### B. Search Card

- Label: “What do you want to study?”
- Input hint: “Search programs, subjects, or universities”
- Primary action: “Search”

Near-term behavior may route to Programs. Do not imply full cross-entity search until supported.

### C. Quick Chips Or Suggestions

Possible examples:

- Bachelor’s
- Master’s
- Computer Science
- Germany
- Scholarships

These are future design guidance. A chip may become interactive only when a real supported filter or destination exists.

### D. Main Browse Cards

- **Programs:** “Compare degrees by tuition, duration, and language.”
- **Universities:** “Explore institutions and their programs.”
- **Countries:** “Plan where to study abroad.”

Each card should have one clear destination and a concise visual identity. Avoid feature grids with dense explanatory copy.

### E. Fit Finder CTA

- Heading: “Not sure where to start?”
- Supporting copy: “Answer a few questions and find programs that match your goals.”
- Later login explanation: “Sign in to save your Fit Finder results.”

The CTA may appear as clearly deferred guidance, but must not imply a working Fit Finder before Bundle 15.

### F. Featured Content

Possible sections:

- Featured programs
- Popular countries
- Latest guides
- Scholarships

Show only real data. Prefer compact horizontal carousels or small cards, not giant vertical blocks. Omit a section entirely when its API or content is unavailable.

### G. Trust Note

Use small supporting text:

“DegreeWiki is independent. Always confirm final details on official university or scholarship pages.”

## 6. Program List Blueprint

Program cards should support rapid comparison without becoming detail pages.

Recommended content order:

1. Program title
2. University name
3. City and country
4. Degree-level badge and subject badge
5. Mini facts row: tuition, duration, and language when available
6. Deadline badge when available
7. Public-safe verification or source note when available

Rules:

- Omit missing facts instead of showing “not available” repeatedly.
- Never invent deadline, tuition, language, or source data.
- Limit title and supporting text to readable line counts.
- Keep descriptions out of the card unless a short, useful teaser is genuinely needed.
- Keep badges few, compact, and semantically meaningful.

## 7. Program Detail Blueprint

### A. Hero/Header

- Program title
- University name
- City and country
- Degree level, language, and verification badges when available

### B. Action Row

- Save
- Compare
- Official page or Apply link when available

Only expose actions that work. Save may be login-gated; Compare behavior remains an open decision.

### C. Key Facts Card

- Tuition
- Duration
- Language
- Study mode
- Delivery mode
- Intake or deadline

Each fact is conditional and should use the clearest public display value provided.

### D. Structured Sections

1. Overview or summary
2. Tuition and fees
3. Admission requirements
4. English requirements
5. Intakes and deadlines
6. Curriculum
7. Career outcomes
8. Source and verification
9. More programs at this university

Rules:

- Show only sections containing real data.
- Never create fake requirements or source claims.
- Make official links explicit actions in a later implementation bundle.
- Use compact related-program cards.

## 8. University List Blueprint

Target university card:

- Optional logo or initials
- University name
- City and country
- One-line or maximum two-line overview teaser
- Available facts or badges: program count, verified/partially verified state, official site available

Rules:

- Never show a raw `countryId`.
- Never show a full overview or long paragraph in a list.
- Limit teaser text to two lines.
- Card tap may provide the implicit “View profile” action; a redundant button is not required.
- Show verification only when backed by real public data.

## 9. University Detail Blueprint

### A. Hero/Header

- University name
- City and country
- Official website action when available
- Verification badge when available

### B–H. Structured Content

1. Overview
2. Key facts
3. Admissions
4. International student support
5. Scholarships and housing
6. Programs at this university
7. Source and verification

Rules:

- Render related programs as compact cards.
- Omit sections with no real content.
- Break long copy into readable, descriptive sections.
- Do not infer services or formal verification.

## 10. Country List Blueprint

Target country card:

- Image when available; otherwise a clean initials, flag, or country-code treatment
- Country name
- Region, continent, or currency when available
- Two-line destination summary
- Compact facts such as university/program count or tuition/living-cost cue when available

Rules:

- Do not show full destination descriptions or huge paragraphs in the list.
- Keep summary copy to two lines.
- Reserve full guidance for country detail.
- Do not synthesize cost cues from incomplete data.

## 11. Country Detail Blueprint

Recommended structure:

1. Country name
2. Short study-destination overview
3. Quick facts
4. Tuition and living costs
5. Visa and work guidance
6. Education system and admissions
7. Universities in this country
8. Programs in this country
9. Scholarships when supported later
10. Official sources

Rules:

- Never invent or extrapolate visa, work, admissions, or cost rules.
- Show official source links when the API provides them.
- Use a clear, compact reminder to verify requirements with official authorities and institutions.
- Omit missing facts and empty sections.

## 12. Profile And Login Blueprint

The logged-out Profile should evolve from form-first to benefit-first.

- Heading: “Create your DegreeWiki account”
- Benefits:
  - Save programs
  - Keep Fit Finder results
  - Compare options
  - Continue across devices
- Primary action: “Log in”
- Secondary action: “Create account,” only if registration is supported

The supported email/password form may appear after the chosen action or remain on the same screen if the current flow requires it.

Rules:

- Do not show fake Google login or unsupported authentication methods.
- Do not promise account features until their behavior is implemented.
- Keep validation and error copy specific, calm, and actionable.

## 13. Fit Finder Placement

Fit Finder should be visible as a future student aid, but should not become a bottom tab yet.

Preferred placements:

- Home CTA
- Profile/dashboard after login
- Possibly Program detail later

Preferred copy:

“Not sure where to start? Find programs matched to your goals.”

Avoid “AI dashboard,” “generated recommendations,” and guaranteed match or admission language. Fit Finder must use backend-scored real programs only. A login gate may protect saved results; anonymous trial behavior remains an open decision.

## 14. Chat Placement

Chat should support discovery contextually rather than dominate the app.

Preferred placements:

- Small Home help card
- Contextual support inside a Fit Finder result
- Possible help action on Program detail later

Rules:

- Do not place Chat in bottom navigation.
- Do not use AI-first product positioning.
- Use the backend AI gateway or approved static answers only.
- Do not implement local AI decision logic.
- Make limitations clear and never imply admission certainty.

## 15. Scholarships And Guides Placement

Near-term, Scholarships and Guides should appear as Home entry cards or an Explore section. They may receive public list/detail screens only after dedicated, verified API support exists.

### Scholarship Card

- Scholarship name
- Provider
- Amount when available
- Deadline when available
- Short eligibility teaser
- Verified or source cue when supported

### Guide Card

- Title
- Category
- Read time or date when available
- Short teaser
- Image when available

All content must be real. An entry card must not imply a live destination before navigation and data are implemented.

## 16. Visual Design System Guidance

- Use a warm off-white page canvas and white cards.
- Use navy for headings, slate for secondary text, and academic blue for primary actions.
- Reserve green badges for real verification states and amber badges for real deadlines.
- Use subtle borders or restrained shadows rather than heavy elevation.
- Favor consistent medium-to-generous card radius and spacing; exact tokens should be finalized during Bundle 9.
- Use readable one-column mobile layouts.
- Use horizontal carousels for compact featured content where they improve browsing.
- Use bottom sheets for future filters and sorting.
- Keep touch targets comfortable and action labels explicit.
- Avoid crowded grids, oversized marketing heroes, and long undifferentiated text blocks.

## 17. Implementation Bundle Plan

### Bundle 9 — Home + Public List Redesign

- Rewrite student-facing copy.
- Redesign Home hierarchy.
- Compact program, university, and country cards.
- Remove long paragraphs from list cards.
- Use the existing API and cache contract; add no new API behavior.

### Bundle 10 — Detail Screen Redesign

- Build stronger rich detail pages using the existing detail API.
- Make supported official links clickable.
- Organize content into structured conditional sections.

### Bundle 11 — Search + Filter UX

- Add the search input behavior.
- Add a filter bottom sheet.
- Add supported chips, filters, and sorting.

### Bundle 12 — Scholarships/Guides API

- Add the required public API work in the web repo as a separate scoped bundle.

### Bundle 13 — Scholarships/Guides Android

- Add Android public list/detail experiences using the verified API.

### Bundle 14 — Profile/Saved Items

- Add benefit-led account UX, saved items, and dashboard behavior.

### Bundle 15 — Fit Finder

- Add a native, login-aware Fit Finder flow using backend-scored real programs.

### Bundle 16 — Chat

- Add contextual Chat through the backend gateway; do not make it a bottom tab.

## 18. Open Decisions

- **Resolved for Bundle 9:** keep `Countries` in bottom navigation for narrow-screen usability and use `Study destinations` as the screen heading.
- Should Home featured content use horizontal carousels or vertical cards?
- Should Compare be public and local before login?
- When, if ever, should Saved become a bottom tab?
- Should Fit Finder require login immediately or allow an anonymous trial if the backend supports it?
- Which mobile search filters should be supported first?

## Bundle 9 Implementation Notes

- Home featured content uses compact horizontal rows with at most three real records per section.
- The Home search entry routes to Programs until real search behavior is implemented.
- No Scholarships or Guides entry cards are rendered yet because they have no supported Android navigation.
- Program, university, and country list cards use only the existing Room-backed domain fields.
- Remote logos and destination images remain deferred; initials are used without adding an image-loading dependency.
- Fit Finder appears only as a clearly deferred, non-clickable card.
