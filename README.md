# Upcoming Movies

An Android application that displays upcoming movies fetched from [The Movie Database (TMDB) API](https://www.themoviedb.org/), with offline support via local caching and a detail screen for each movie.

---

## Purpose

The app allows users to:
- Browse a list of upcoming theatrical releases sorted by release date and rating.
- See the movie poster, title, release date (BR locale), and rating at a glance.
- Identify unreleased movies with a dynamic countdown ("Release in X days").
- Tap a movie to open a detail screen with backdrop, tagline, genres, overview, runtime, and status.
- Use the app offline — the list is cached locally and served from the database while a background refresh runs.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| ViewModel | Jetpack ViewModel + StateFlow |
| Dependency Injection | Koin 4 |
| Networking | Retrofit 2 + OkHttp 4 + Gson |
| Local cache | Room 2 |
| Image loading | Coil 2 |
| Async | Coroutines + Flow |
| Build | AGP 9 · Kotlin 2.1 · KSP · Gradle version catalogs |

---

## Architecture

The project follows **Clean Architecture** with a **feature-first** package structure. All layers live inside the `:app` module, organised by feature rather than by layer type.

### Package structure

```
com.example.upcomingmovies
├── feature/
│   ├── core/                          # Cross-feature utilities
│   │   ├── extensions/
│   │   │   ├── ComponentPreview.kt    # Custom @Preview annotation
│   │   │   └── DateExtensions.kt     # formatToBrDate, daysUntilRelease, formatRuntime …
│   │   └── ui/
│   │       ├── AppNavigation.kt       # NavHost — owns the back stack
│   │       ├── MainActivity.kt
│   │       └── UpcomingMoviesApp.kt   # Koin initialisation
│   │
│   ├── movielist/                     # Movie list feature
│   │   ├── data/
│   │   │   ├── local/                 # Room entity, DAO, AppDatabase
│   │   │   ├── remote/                # Retrofit DTO + MovieService
│   │   │   ├── mapper/                # DTO → Entity, Entity → Domain
│   │   │   └── repository/            # MovieRepositoryImpl
│   │   ├── domain/
│   │   │   ├── model/                 # Movie (domain model)
│   │   │   ├── repository/            # MovieRepository (interface)
│   │   │   └── usecase/               # ObserveMoviesUseCase, RefreshMoviesUseCase
│   │   ├── di/
│   │   │   └── MovieListModule.kt     # Koin module (DB, network, repo, VM)
│   │   └── presentation/
│   │       ├── viewmodel/             # MovieListState, MovieListAction, MovieListViewModel
│   │       ├── components/            # MovieItem, MovieList, LoadingContent, ErrorContent
│   │       ├── MovieListScreen.kt     # Route + Screen composables
│   │       └── MovieListPreviewProvider.kt
│   │
│   └── moviedetails/                  # Movie detail feature
│       ├── data/
│       │   ├── remote/                # Retrofit DTO + MovieDetailService
│       │   ├── mapper/                # DTO → Domain
│       │   └── repository/            # MovieDetailRepositoryImpl
│       ├── domain/
│       │   ├── model/                 # MovieDetail (domain model)
│       │   ├── repository/            # MovieDetailRepository (interface)
│       │   └── usecase/               # GetMovieDetailUseCase
│       ├── di/
│       │   └── MovieDetailModule.kt   # Koin module (service, repo, usecase, VM)
│       └── presentation/
│           ├── viewmodel/             # MovieDetailState, MovieDetailAction, MovieDetailViewModel
│           ├── components/            # MovieDetailHeader, MovieDetailGenres, MovieDetailInfo
│           ├── MovieDetailScreen.kt   # Route + Screen composables
│           └── MovieDetailPreviewProvider.kt
```

### Layers

```
UI (Compose)
    │  collects StateFlow, dispatches Actions
    ▼
ViewModel
    │  calls use cases, exposes sealed State
    ▼
Use Case  (domain — no Android deps)
    │  delegates to repository interface
    ▼
Repository (interface in domain, impl in data)
    │
    ├──▶ LocalDataSource  (Room / DAO)   ← SSOT for movie list
    └──▶ RemoteDataSource (Retrofit)     ← sync input
```

### Offline-first (movie list)

The movie list is **offline-first**: Room is the single source of truth.

1. `MovieListViewModel` collects `ObserveMoviesUseCase` — a `Flow` backed by Room — so the UI always renders from the local database.
2. On launch (and on retry) `RefreshMoviesUseCase` fetches from the TMDB API and upserts results into Room.
3. Room emits the updated list through the `Flow`, which the ViewModel picks up automatically.
4. If the network call fails while the database already has data, the cached list stays visible and no error is shown.

The movie detail screen does **not** cache locally — it is a one-shot `suspend` call that fetches on demand.

### State & Action pattern

Every screen uses a `sealed class` for state and a `sealed class` for actions (MVI-lite):

```kotlin
// Example — movie list
sealed class MovieListState {
    data object Loading : MovieListState()
    data class Success(val movies: List<Movie>) : MovieListState()
    data class Error(val message: String) : MovieListState()
}

sealed class MovieListAction {
    data object Refresh : MovieListAction()
    data object RetryLoad : MovieListAction()
}
```

The ViewModel exposes a single `StateFlow<State>` and a single `onAction(Action)` entry point. Composables observe the state and forward user events as actions.

### Dependency injection (Koin)

Each feature owns a single Koin module that wires its entire vertical slice:

```kotlin
val movieListModule = module {
    // Database
    single { Room.databaseBuilder().build() }
    single { get<AppDatabase>().movieDao() }
    // Network
    single { OkHttpClient.Builder() }
    single { Retrofit.Builder() }
    single { get<Retrofit>().create(MovieService::class.java) }
    // Domain
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    factory { ObserveMoviesUseCase(get()) }
    factory { RefreshMoviesUseCase(get()) }
    // Presentation
    viewModel { MovieListViewModel(get(), get()) }
}
```

### Navigation

`AppNavigation.kt` owns a `NavHost` with two string routes:

| Route | Screen |
|---|---|
| `movie_list` | `MovieListRoute` |
| `movie_detail/{movieId}` | `MovieDetailRoute` |

`MovieDetailViewModel` receives `movieId` as a constructor parameter via Koin's `parametersOf`.

---

## Setup

1. Clone the repository.
2. Obtain a **TMDB API Read Access Token** from [themoviedb.org/settings/api](https://www.themoviedb.org/settings/api).
3. Open `feature/movielist/di/MovieListModule.kt` and replace the `TMDB_ACCESS_TOKEN` constant with your token.
4. Build and run.

> The token is a JWT (Bearer auth). Do **not** commit it to source control — consider moving it to `local.properties` and reading it via `BuildConfig` before sharing the project.

---

## API Reference

| Endpoint | Used for |
|---|---|
| `GET /movie/upcoming` | Upcoming movie list |
| `GET /movie/{movie_id}` | Movie detail |

Base URL: `https://api.themoviedb.org/3/`  
Image base URL: `https://image.tmdb.org/t/p/{size}{path}`
