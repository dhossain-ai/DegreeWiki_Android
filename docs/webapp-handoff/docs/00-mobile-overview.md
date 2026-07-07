# Mobile Overview

DegreeWiki on Android should be a native Kotlin app built with Jetpack Compose.

The app should be API-driven, not a WebView shell. Screens should read data from DegreeWiki endpoints, render content natively, and keep enough local state to support offline viewing of recently opened or saved information.

Core expectations for the Android app:

- Kotlin native UI
- Jetpack Compose for screens and navigation
- API-driven data flow
- offline-readable saved and recently viewed content
- native Fit Finder and chat screens that call backend APIs
- no direct dependency on the webapp SQL migration files

Recommended product direction:

- browse public DegreeWiki content natively
- cache useful content for offline reading
- keep saved items and recent history available locally
- let Fit Finder and AI chat feel native while still using server APIs for the actual logic
- use the webapp only as the system of record for data, auth, media, and AI services