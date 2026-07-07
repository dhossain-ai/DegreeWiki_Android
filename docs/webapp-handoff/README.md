# DegreeWiki Webapp Handoff

This folder is a reference handoff package copied from the DegreeWiki webapp repo for the Android repo to use as context only.

Use it to plan and build a native Kotlin Android app with Jetpack Compose, not a WebView wrapper and not a copy of the webapp codebase.

The Android app should call DegreeWiki APIs, keep its own Room cache and DataStore settings, and avoid any direct dependency on the webapp SQL migration files.

No secrets are included in this package.

## What is included

- copied webapp reference docs from `W:\DegreeWiki\docs`
- mobile-oriented summary docs for Android planning
- route inventory files for APIs, auth, Cloudinary, AI, dashboard, and Supabase-related code

## What is intentionally excluded

- `.env` files and local env overrides
- service-role keys and other private secrets
- Cloudinary API secrets
- AI provider keys
- Wrangler secrets
- build output, caches, and local temp folders
- `node_modules`, `.git`, `dist`, `.astro`, `.wrangler`, and Supabase temp folders
- Android implementation code

## Source notes

The source webapp uses Astro, Supabase Auth/PostgreSQL/RLS, Cloudflare deployment, Cloudinary media delivery, an AI Gateway layer, Fit Finder, saved results/chat, and admin/dashboard flows.

Copy status for the requested docs:

- `docs/00-project-overview.md` copied
- `docs/01-product-decisions.md` copied
- `docs/02-architecture.md` copied
- `docs/03-database-plan.md` copied
- `docs/04-ai-system.md` copied
- `docs/05-coding-standards.md` copied
- `docs/06-status.md` copied
- `docs/07-task-log.md` copied

If any of the above files go missing in the source repo later, the Android handoff should treat this folder as the preserved reference copy.