# Webapp Context

This is the Android-relevant context from the DegreeWiki webapp repo.

## Product purpose

DegreeWiki is an SEO-focused education information platform for international students. It helps users discover and compare programs, scholarships, universities, countries, subjects, guides, and AI-assisted best-fit recommendations.

## Current web stack

- Astro for the main web frontend
- React islands for interactive parts of the UI
- Tailwind CSS styling
- Supabase PostgreSQL as the source of truth
- Supabase Auth for identity
- Supabase RLS for access control
- Cloudflare Pages/Workers for deployment
- Cloudinary for public media delivery
- an internal AI Gateway abstraction for LLM calls

## Auth model

- Supabase Auth handles login identity
- server endpoints verify sessions with the user JWT
- browser or app code should not receive privileged server keys
- admin and contributor access is permissioned separately from regular student access

## Supabase and RLS rule

Public content is readable only when published and indexable. Owner-only data such as saved items, student profiles, AI history, and contributor-specific state is protected by RLS or server-side permission checks.

## Cloudinary rule

Cloudinary stores and delivers public images. Supabase/PostgreSQL stores media metadata. The app should consume image URLs, not Cloudinary secrets.

## AI Gateway rule

AI features are routed through server-side gateway code. The webapp uses AI for Fit Finder explanations, saved-result chat, site chat, static preset answers, and admin article assistance. The mobile app should call backend APIs that enforce the same safety and quota rules.

## Public content entities

The webapp centers on countries, universities, programs, scholarships, guides/articles, and destination-style public pages.

## Student dashboard and saved-result context

The webapp already has authenticated saved-program flows, Fit Finder result pages, AI conversations, and account/dashboard surfaces. Those are the main sources for Android cache, saved history, and authenticated user state.

## Contributor context

Contributor review, public contributor profiles, avatar upload/review, and contributor account pages exist in the webapp. These are useful reference points if Android later needs contributor-facing features.

## Cloudflare deployment context

The webapp is built for Cloudflare Pages/Workers and avoids Node-only runtime dependencies in server paths. Any Android-facing backend work should stay compatible with that deployment model.