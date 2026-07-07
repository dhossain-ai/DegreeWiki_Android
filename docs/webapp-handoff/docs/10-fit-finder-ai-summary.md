# Fit Finder and AI Summary

## Routes and APIs found

Fit Finder and chat-related behavior is split across these webapp routes:

- `src/pages/api/ai/finder-summary.ts`
- `src/pages/api/ai/chat.ts`
- `src/pages/api/ai/chat-clear.ts`
- `src/pages/api/ai/site-chat.ts`
- `src/pages/api/ai/site-chat-session.ts`
- `src/pages/api/ai/site-chat-clear.ts`
- `src/pages/fit-finder/results/[id].astro`
- `src/pages/fit-finder/result.astro`

## AI Gateway files found

- `src/lib/ai/gateway.ts`
- `src/lib/ai/router.ts`
- `src/lib/ai/env.ts`
- `src/lib/ai/types.ts`
- `src/lib/ai/usage/limits.ts`
- `src/lib/ai/usage/logging.ts`
- `src/lib/ai/prompts/finder-summary.ts`
- `src/lib/ai/prompts/chat-answer.ts`
- `src/lib/ai/chat/router.ts`
- `src/lib/ai/chat/context.ts`
- `src/lib/ai/chat/persist.ts`
- `src/lib/ai/finder/persist.ts`
- `src/lib/ai/site-chat/router.ts`
- `src/lib/ai/site-chat/context.ts`
- `src/lib/ai/site-chat/persist.ts`
- `src/lib/ai/site-chat/static-answers.ts`

## Mobile strategy

The Android app should treat Fit Finder as a native flow:

- collect user preferences in Compose screens
- send the profile to a backend endpoint
- receive a real shortlist of programs
- cache the result locally for offline viewing
- show an AI explanation only when the backend returns one

For chat, the Android app should use a native conversation screen that still calls backend APIs for the actual answer generation and safety checks.

## What must remain server-side

Keep these on the backend:

- AI provider keys
- AI Gateway routing and guardrails
- quota checks and usage logging
- explanation generation
- saved-result persistence
- conversation persistence
- static preset-answer routing
- contributor or admin AI workflows

## Important behavior to preserve

- Fit Finder should only explain real shortlisted programs
- the AI must not invent programs or guarantees
- static site-chat responses can answer common questions without calling AI
- saved-result chat and global site chat are separate behaviors
- Android should mirror the server’s safety and quota boundaries rather than recreate them locally