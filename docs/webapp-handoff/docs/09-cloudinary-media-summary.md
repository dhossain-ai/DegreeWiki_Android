# Cloudinary Media Summary

## Helper files found

- `src/lib/cloudinary/config.ts`
- `src/lib/cloudinary/url.ts`
- `src/lib/cloudinary/upload.ts`
- `src/lib/cloudinary/folders.ts`
- `src/lib/public/media.ts`
- `src/lib/admin/media.ts`
- `src/components/admin/MediaPicker.astro`

## Upload endpoints found

- `src/pages/api/admin/media/sign-upload.ts`
- `src/pages/api/admin/media/import-url.ts`
- `src/pages/api/admin/media/complete-upload.ts`
- `src/pages/api/contributor/avatar/sign-upload.ts`
- `src/pages/api/contributor/avatar/complete-upload.ts`

## URL generation helpers

- `cloudinaryUrl()` builds delivery URLs from a cloud name and public id
- `getOgImageUrl()` selects a safe OG image URL when public media is available
- public media helpers avoid exposing secrets and are safe to consume from app code

## Media table and metadata pattern

The webapp stores media metadata in PostgreSQL rather than in the Android app. The important pattern is that Cloudinary holds the image, while the database stores the public id, alt text, captions, attribution, and relationship metadata.

## What Android should consume

Android should consume:

- public image delivery URLs
- safe alt text
- display names
- media ids or public ids only when needed for display logic

Android should not consume:

- Cloudinary API secrets
- upload signatures
- server signing logic
- admin upload/import behavior

## Backend-only responsibilities

Keep these server-side:

- Cloudinary signature generation
- upload verification
- import-from-URL workflows
- media asset insertion and moderation
- contributor avatar upload approval

## Security reminder

Cloudinary secrets must not be placed in the Android app. The app should only receive already-generated image URLs or other safe delivery metadata.