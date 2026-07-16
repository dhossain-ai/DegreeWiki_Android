# Android Coding Standards

Last audited: 2026-07-16

## Bundle 15 Authenticated Request And Saved-State Rules

- Mark only protected Retrofit methods for bearer injection; never authenticate public routes by
  default.
- Read the access token at request time from the current Supabase session. Never hardcode, log,
  display, or persist a token outside the existing encrypted session manager.
- Redact `Authorization` in all OkHttp logging.
- Treat `401` as an expired/logged-out session, not as a raw UI error.
- Never send a client-selected user ID to authenticated account endpoints.
- Keep both Program ID and saved-item ID. Program UI keys by Program ID; DELETE keys by saved-item
  ID.
- Serialize save mutations, disable repeat taps while pending, and preserve idempotency locally.
- Partition authenticated Room rows by owner and clear active saved state on logout/account change.
- Do not clear public browse caches when authentication changes.
- Use exact documented profile fields and omit unsupported identity, role, preference, and student
  metadata.

## Standards Already Followed

- Kotlin-first implementation
- Compose-first UI
- Hilt modules for app-wide dependencies
- Repository interfaces backed by injectable implementations
- DTO, entity, and domain model separation for main public data sets
- `StateFlow`-based screen state in active view models

## Standards To Keep

- Document only repo-verified features and contracts
- Keep Android client data-backed only
- Preserve current working behavior unless a task explicitly changes it
- Prefer additive changes over large architectural rewrites
- Keep backend/web assumptions out of Android docs unless verified by Android source

## Current Code Quality Risks To Watch

- Do not leave placeholder UI wired as if it were a shipped feature
- Avoid swallowing repository exceptions without exposing useful UI state
- Avoid introducing new DTO fields or fake data for missing product needs
- Avoid route keys or screens that are not actually connected
- Avoid broad `BODY` request/response logging in production-facing builds unless intentionally controlled

## Current Repo-Specific Guidance

### UI

- Use lifecycle-aware state collection in Compose where possible
- Keep screen state explicit and typed
- Prefer reusable state components once list/detail states become more complex

### Data

- Map network DTOs before they reach UI
- Keep Room entities aligned to actual offline needs
- Be deliberate about destructive migration usage

### Auth

- Treat Supabase session state as the auth source of truth
- Keep bearer-token attachment centralized in the interceptor

### Navigation

- Only add nav destinations that have real screens and real data paths
- Prefer consistent route usage instead of mixing local tab state and unused nav keys

### Testing

- Update or remove stale tests as soon as a validation-focused phase allows code changes
- Do not trust green docs over red tests

## Documentation Standards

- Mark unverified items explicitly
- Mark placeholder or deferred features explicitly
- Use the compact status file as the active truth source
- Record each audit or implementation phase in the task log
