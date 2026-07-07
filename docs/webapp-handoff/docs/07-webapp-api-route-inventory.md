# Webapp API Route Inventory

This file lists the existing webapp API routes and how useful they are for Android planning.

| Route path | File path | Likely purpose | Auth | Mobile suitability |
|---|---|---|---|---|
| `/api/search-suggestions` | `src/pages/api/search-suggestions.ts` | Public search autocomplete for programs, subjects, universities, and destinations | Public | reusable as-is |
| `/api/saved-items/program` | `src/pages/api/saved-items/program.ts` | Save or unsave a program for the signed-in user | Auth required | needs wrapper |
| `/api/auth/logout` | `src/pages/api/auth/logout.ts` | Sign the user out of Supabase session auth | Auth required | needs wrapper |
| `/api/ai/finder-summary` | `src/pages/api/ai/finder-summary.ts` | Generate or return a cached Fit Finder explanation | Auth required | needs wrapper |
| `/api/ai/chat` | `src/pages/api/ai/chat.ts` | Saved-result AI chat for a logged-in user | Auth required | needs wrapper |
| `/api/ai/chat-clear` | `src/pages/api/ai/chat-clear.ts` | Clear a saved-result chat conversation | Auth required | needs wrapper |
| `/api/ai/site-chat-session` | `src/pages/api/ai/site-chat-session.ts` | Load the current global site-chat session and history | Mixed: public shell, auth for history | needs wrapper |
| `/api/ai/site-chat-clear` | `src/pages/api/ai/site-chat-clear.ts` | Clear the global site-chat conversation | Auth required | needs wrapper |
| `/api/ai/site-chat` | `src/pages/api/ai/site-chat.ts` | Send a site-chat message, with static/preset/AI routing | Mixed: public static path, auth for AI | needs wrapper |
| `/api/contributor/avatar/sign-upload` | `src/pages/api/contributor/avatar/sign-upload.ts` | Sign a contributor avatar Cloudinary upload | Auth required | not suitable for mobile |
| `/api/contributor/avatar/complete-upload` | `src/pages/api/contributor/avatar/complete-upload.ts` | Finalize contributor avatar upload and persist asset data | Auth required | not suitable for mobile |
| `/api/admin/media/sign-upload` | `src/pages/api/admin/media/sign-upload.ts` | Sign admin media uploads to Cloudinary | Admin required | not suitable for mobile |
| `/api/admin/media/import-url` | `src/pages/api/admin/media/import-url.ts` | Import a remote image through Cloudinary and store media metadata | Admin required | not suitable for mobile |
| `/api/admin/media/complete-upload` | `src/pages/api/admin/media/complete-upload.ts` | Finalize admin media upload and insert media metadata | Admin required | not suitable for mobile |
| `/api/admin/articles/ai-assist` | `src/pages/api/admin/articles/ai-assist.ts` | Admin article AI drafting assistance | Admin required | not suitable for mobile |
| `/api/admin/ai-knowledge/answers` | `src/pages/api/admin/ai-knowledge/answers.ts` | CRUD and bulk actions for reviewed site-chat preset answers | Admin required | not suitable for mobile |
| `/api/admin/ai-knowledge/import` | `src/pages/api/admin/ai-knowledge/import.ts` | Import reviewed preset answers from JSON | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/health` | `src/pages/api/admin/ai-gateway/health.ts` | Inspect and reset AI provider health | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/models` | `src/pages/api/admin/ai-gateway/models.ts` | Manage AI models | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/policies` | `src/pages/api/admin/ai-gateway/policies.ts` | Manage AI routing policies | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/providers` | `src/pages/api/admin/ai-gateway/providers.ts` | Manage AI provider accounts and encrypted keys | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/test` | `src/pages/api/admin/ai-gateway/test.ts` | Preset-only AI Gateway test calls | Admin required | not suitable for mobile |
| `/api/admin/ai-gateway/usage-limits` | `src/pages/api/admin/ai-gateway/usage-limits.ts` | Manage AI usage limit policies | Admin required | not suitable for mobile |

## Practical takeaway

For Android, the safest path is to keep these webapp routes as reference behavior and introduce mobile-specific endpoints for token-based auth, cache-friendly payloads, and stable versioning.