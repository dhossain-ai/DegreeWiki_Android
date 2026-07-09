# Android Project Overview

Last audited: 2026-07-09

## Purpose

DegreeWiki Android is a native Kotlin app for a student-first education search product. This repo is an Android-only codebase, not the main web/backend repo.

This file reflects the Android repo as verified in source during the 2026-07-09 audit. Older docs under `docs/webapp-handoff/` are context only and are not the current Android truth.

## Verified Repo Shape

- Single-module Android app: `:app`
- Native Kotlin app with Jetpack Compose UI
- No XML layout files in active UI flow
- Hilt dependency injection
- Retrofit + OkHttp for HTTP
- Room for local persistence
- Supabase Auth client for login/session state
- Experimental Navigation 3 API for app navigation

## Top-Level Structure

- `app/`
  Android application module
- `docs/`
  Android audit docs plus older web handoff material
- `gradle/`
  wrapper and version catalog

## Kotlin Package Structure

- `com.example.degreewiki`
  app entry points
- `data.local`
  Room database, saved-item DAO/entity, database module
- `data.local.dao`
  DAOs for programs, universities, countries
- `data.local.entity`
  Room entities for programs, universities, countries
- `data.mapper`
  DTO-to-entity and entity-to-domain mappers
- `data.network`
  Retrofit API interface, network module, auth wiring
- `data.network.dto`
  mobile DTOs
- `data.repository`
  repository interfaces and implementations
- `domain.model`
  app-facing models
- `ui.features.*`
  feature screens and view models
- `ui.navigation`
  Navigation 3 keys and root nav host
- `ui.theme`
  Compose theme, colors, typography

## Verified Current User-Facing Surface

- Main tab shell
- Programs list
- Universities list
- Countries list
- Program detail
- University detail
- Country detail
- Login form
- Auth-gated profile screen with saved items

## Present But Not In Active Navigation

- `ui.features.discover.DiscoverScreen`
- `ui.features.chat.ChatScreen`
- `ui.features.fitfinder.FitFinderScreen`

These currently contain placeholder text and are not wired into the app navigation.

## Build And Platform Baseline

- Kotlin JVM toolchain 17
- `compileSdk = 36`
- `minSdk = 26`
- `targetSdk = 36`
- Compose enabled
- `buildConfig` enabled for runtime config injection

## Config And Secrets

Runtime config is injected from `local.properties` into `BuildConfig`:

- `API_BASE_URL`
- `SUPABASE_URL`
- `SUPABASE_ANON_KEY`

Verified behavior:

- If missing, the app falls back to placeholder values.
- This means the repo can compile even when real service values are absent, but network/auth behavior will not be real with placeholder config.

## Current Audit Summary

- The app is a real native Android foundation, not a web shell.
- It is smaller than the inherited planning docs suggested.
- Public content currently comes from list endpoints plus Room cache.
- Detail screens read by `id` from cached Room data rather than from dedicated detail endpoints.
- Auth is email/password only in working code.
- Some tests and docs are stale.
