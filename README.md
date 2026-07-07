# DegreeWiki Android App

A production-quality, native Android client for **DegreeWiki** built with modern Android development standards, Kotlin, and Jetpack Compose.

## 🚀 Tech Stack & Libraries
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Dependency Injection:** Dagger Hilt
*   **Local Caching:** Room Database
*   **Network Layer:** Retrofit 2 + OkHttp + Kotlinx Serialization Json
*   **Concurrency:** Kotlin Coroutines & Flow
*   **Navigation:** Jetpack Compose Navigation 3 (Experimental)

---

## 📂 Package Architecture
The app follows a clean, feature-by-package structure to ensure scalability, ease of navigation, and a clear separation of concerns:

```
com.example.degreewiki/
│
├── data/                       # Data layer (Room cache, Retrofit API, repositories)
│   ├── local/                  # Room Database, entities, and DAOs
│   ├── network/                # Retrofit API Service interfaces and NetworkModule
│   └── repository/             # Concrete repository implementations and bindings
│
├── domain/                     # Domain layer (pure business logic)
│   └── model/                  # Domain entity models (e.g., SavedItem)
│
└── ui/                         # Presentation layer (Compose UI components)
    ├── theme/                  # Premium custom colors, typography, shapes, and theme
    ├── navigation/             # Navigation configurations and routes
    └── features/               # Feature-specific packages
        ├── main/               # Main greeting screen
        ├── discover/           # Discover degree programs screen
        ├── auth/               # User registration and login flow
        ├── profile/            # User profile and study preferences
        ├── fitfinder/          # Fit Finder AI tool UI
        └── chat/               # Conversational AI chat assistant
```

---

## 🛠️ Key Architectural Components

### 1. Dependency Injection (Hilt)
*   **`DegreeWikiApplication`**: Marked with `@HiltAndroidApp` as the dependency container's root.
*   **`NetworkModule`**: Provides thread-safe singleton instances of `OkHttpClient` (with logging), `Retrofit`, and `DegreeWikiApiService`.
*   **`DatabaseModule`**: Handles Room database instance builder and exports DAOs as injectables.
*   **`RepositoryModule`**: Binds abstraction interfaces to repository implementations.

### 2. Local Cache (Room)
*   **`DegreeWikiDatabase`**: Outlines the schema and lists available DAOs.
*   **`SavedItemEntity`**: Local table mapping saved programs, scholarships, and universities.
*   **`SavedItemDao`**: Performs database insertions, queries, and deletions using Kotlin Coroutine Flows.

### 3. Network Client (Retrofit)
*   **`DegreeWikiApiService`**: Configured with serialization converters to perform network calls against remote `/api/mobile/*` endpoints.

---

## 🛠️ Verification & Compilation
To verify the project is working and compiles perfectly:
```bash
./gradlew compileDebugSources
./gradlew assembleDebug
```
