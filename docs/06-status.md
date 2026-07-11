# Android Status

Last updated: 2026-07-11

## Bundle 5 Status

- Bundle 5 state: completed
- Scope stayed documentation-only
- No Android feature work was added
- No web repo files were modified

## Current API/Data Contract Truth

- Android public browse currently uses only three thin collection endpoints:
  - `/api/mobile/programs`
  - `/api/mobile/universities`
  - `/api/mobile/countries`
- Android detail screens are cache lookups over Room, not separate mobile detail endpoints.
- The public web app already uses much richer program, university, and destination data than Android currently receives.
- Scholarships and guides exist as public web surfaces, but Android has no verified public mobile endpoints or screens for them yet.

## Most Important Missing Fields

- Programs: language, study mode, delivery mode, city, tuition currency/period, deadlines/intakes, official/application URLs, requirements, verification metadata
- Universities: official URL, ranking/admissions/support summaries, short/native names, verification metadata
- Destinations: ISO/currency/capital facts, tuition/living-cost guidance, visa/work guidance, official URLs, FAQ, verification metadata

## Validation Results

- `./gradlew.bat test` passed
- `./gradlew.bat build` passed
- `./gradlew.bat lint` passed
- `build` emitted one Android SDK XML version warning from the local toolchain, but the build still succeeded

## Next Recommended Bundle

Bundle 6 should expand the public mobile contract in a structured way, starting with the highest-value missing detail fields for programs, universities, and destinations before any scholarships or guides implementation.
# Bundle 7 status — complete (2026-07-11)

- Android consumes the rich slug detail endpoints for programs, universities, and countries.
- Program details show real location, language, modes, tuition display, intake/deadline, requirements, curriculum, career, URLs, and verification metadata when returned.
- University details show real institution facts, ranking, admissions, language, scholarship, housing, student-life/international/career summaries, and official URL when returned.
- Country details show real code/continent/currency/language facts, costs, admissions, visa/work/post-study guidance, official URLs, and FAQ when returned.
- Cached list records render first and remain the fallback for missing slugs, 404s, malformed/partial responses, and network failures. Rich detail is in memory; Room schema and destructive migration behavior are unchanged.
- Automated validation: unit tests, build, and lint (see task log). Manual device QA was not available in this environment.
- Known issue: expanded list-only fields are parsed but not added to Room, so cards retain the existing cache-backed field set; rich additions appear on details.
- Next recommended bundle: focused public-detail polish and device QA, including safe clickable official links and structured intake/related-record presentation.
