# Mobile Architecture

Recommended Android stack for DegreeWiki:

- Kotlin
- Jetpack Compose
- MVVM
- Repository pattern
- Retrofit or Ktor for HTTP calls
- Room for offline cache
- DataStore for small settings and session metadata
- Coil for image loading
- WorkManager later for sync jobs and deadline alerts
- Firebase Cloud Messaging later for push notifications

## Suggested structure

- UI layer: Compose screens, navigation, state holders
- ViewModel layer: screen state, loading, error handling, user actions
- Repository layer: API and cache orchestration
- Data layer: Retrofit/Ktor client, Room DAO, DataStore access, token storage

## Behaviour goals

- render public content natively
- cache important content for offline viewing
- keep saved items and recently viewed content synchronized when online
- keep auth-aware data behind bearer-token calls
- treat Fit Finder and chat as first-class native flows, not embedded web pages

## Practical rules

- prefer clear state models over ad hoc screen logic
- keep network responses mapped into app-specific models before they reach UI
- store only safe, non-sensitive session metadata locally
- let the backend remain the source of truth for permissions, AI, and server-verified operations