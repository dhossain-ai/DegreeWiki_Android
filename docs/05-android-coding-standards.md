# Android Coding Standards

Last audited: 2026-07-09

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
