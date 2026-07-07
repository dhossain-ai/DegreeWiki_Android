# API Contract Draft

This is a draft mobile API contract based on the current webapp routes and the Android requirements.

## Existing webapp API routes found

These routes already exist in the webapp repo, but many of them are browser-oriented and use cookie-based Supabase sessions:

- `/api/search-suggestions`
- `/api/saved-items/program`
- `/api/auth/logout`
- `/api/ai/finder-summary`
- `/api/ai/chat`
- `/api/ai/chat-clear`
- `/api/ai/site-chat`
- `/api/ai/site-chat-session`
- `/api/ai/site-chat-clear`
- `/api/contributor/avatar/sign-upload`
- `/api/contributor/avatar/complete-upload`
- `/api/admin/media/sign-upload`
- `/api/admin/media/import-url`
- `/api/admin/media/complete-upload`
- `/api/admin/articles/ai-assist`
- `/api/admin/ai-knowledge/answers`
- `/api/admin/ai-knowledge/import`
- `/api/admin/ai-gateway/health`
- `/api/admin/ai-gateway/models`
- `/api/admin/ai-gateway/policies`
- `/api/admin/ai-gateway/providers`
- `/api/admin/ai-gateway/test`
- `/api/admin/ai-gateway/usage-limits`

## Mobile reuse guidance

- reusable as-is: `/api/search-suggestions`
- needs wrapper or mobile auth adaptation: saved items, logout, AI chat, Fit Finder summary, site chat, contributor avatar upload
- not suitable for mobile: admin media, admin AI gateway, admin knowledge, admin article assistant endpoints

The Android app should prefer dedicated mobile endpoints instead of calling browser-specific routes directly.

## Proposed mobile endpoints

### GET /api/mobile/bootstrap

- Auth: optional
- Purpose: return app bootstrap data such as featured content, current app configuration, supported locales, and initial cache hints.
- Request: no body.
- Response summary: `ok`, `site`, `feature_flags`, `bootstrap_content`, `offline_cache_policy`.

### GET /api/mobile/programs/search

- Auth: public for published content
- Purpose: program search with query, filters, sorting, and pagination.
- Request: query parameters such as `q`, `country`, `university`, `subject`, `degree_level`, `page`, `sort`.
- Response summary: `items`, `page`, `page_size`, `total`, `facets`, `next_page`.

### GET /api/mobile/programs/{slug}

- Auth: public for published content
- Purpose: fetch one published program detail record.
- Response summary: program fields, university summary, country summary, subject summary, media URLs, source metadata, related content hints.

### GET /api/mobile/universities/{slug}

- Auth: public for published content
- Purpose: fetch one published university record.
- Response summary: university profile, key facts, location, media URLs, related programs, verification metadata.

### GET /api/mobile/scholarships/{slug}

- Auth: public for published content
- Purpose: fetch one published scholarship record.
- Response summary: scholarship details, eligibility, source links, related program/country/university hints.

### GET /api/mobile/guides/{slug}

- Auth: public for published content
- Purpose: fetch one published guide or article.
- Response summary: title, summary, body format, canonical URL, author/source info, related entities.

### GET /api/mobile/me

- Auth: required
- Purpose: current user profile, permissions, saved-state summary, and lightweight app settings.
- Request: bearer token via `Authorization: Bearer <access_token>`.
- Response summary: auth identity, role hints, profile data, student profile summary, contributor state if relevant.

### GET /api/mobile/me/saved-items

- Auth: required
- Purpose: list the user’s saved items with pagination and minimal denormalized content.
- Request: bearer token.
- Response summary: saved item rows, entity type, entity id, title, slug, thumbnail, saved_at.

### POST /api/mobile/me/saved-items

- Auth: required
- Purpose: save an item from the mobile app.
- Request: JSON with `entity_type`, `entity_id`.
- Response summary: saved row or id, `saved: true`.

### DELETE /api/mobile/me/saved-items/{id}

- Auth: required
- Purpose: remove one saved item by saved-item id.
- Request: bearer token.
- Response summary: `saved: false`.

### POST /api/mobile/reports

- Auth: optional or required depending on abuse policy
- Purpose: submit a report or correction against public content.
- Request: `entity_type`, `entity_id`, `message`, optional contact metadata if allowed.
- Response summary: report id, status, acknowledgement text.

### POST /api/mobile/fit-finder

- Auth: optional for anonymous trial, required for saved-profile mode if desired
- Purpose: submit a native Fit Finder profile and request a scored shortlist.
- Request: study goals, budget, countries, subject, degree level, language, intake, constraints.
- Response summary: `result_id`, shortlist, scores, warnings, cached flags, AI summary availability.

### GET /api/mobile/fit-finder/results/{id}

- Auth: required
- Purpose: fetch one saved Fit Finder result for the owner.
- Response summary: result status, shortlist, explanation, cached data, generated time, related chat availability.

### POST /api/mobile/ai/chat

- Auth: required for personalized chat; optional anonymous mode can be supported later only if carefully rate-limited
- Purpose: native AI chat over DegreeWiki context.
- Request: `message`, optional `context_type`, optional `entity_id` or `result_id`.
- Response summary: answer text, answer source, conversation id, usage state, fallback state.

## Compatibility notes

- Existing webapp routes should remain browser-compatible.
- The Android app should use bearer tokens where possible, not cookie dependencies.
- Sensitive actions should continue to be server-validated.
- Mobile endpoints should return safe, normalized payloads rather than raw database shapes.
- No secrets should ever appear in request or response bodies.