# Auth and Security Rules

The Android app must follow the same security boundaries as the webapp.

## Non-negotiable rules

- the app must never include the Supabase service-role key
- the app must never include the Cloudinary API secret
- the app must never include AI provider keys
- the app must never depend directly on SQL migration files
- the app must not bypass server-side permission checks

## Allowed secret usage

- the app can use the public Supabase anon key only if needed for safe public reads
- authenticated API calls should send the user access token as `Authorization: Bearer <token>`
- server code must validate the token or session before returning private data

## Server-side enforcement

- RLS and business rules stay on the server
- saved items, private profiles, AI requests, contributor actions, and media uploads should be validated server-side
- AI/chat/Cloudinary upload/contributor actions should go through backend APIs, not direct client-side secret use

## Mobile identity model

- Android should treat Supabase Auth as the login identity provider unless the backend defines a different mobile auth flow later
- local storage should only keep safe session metadata or app state
- refresh tokens, access tokens, and session data should be handled carefully and stored only where appropriate for the chosen auth flow

## Practical cautions

- do not mirror service-role logic into the Android app
- do not put media upload secrets into the APK
- do not place AI prompt or provider keys in app resources
- keep privileged write actions behind endpoints that the backend controls