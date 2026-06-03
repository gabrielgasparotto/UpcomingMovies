# Upcoming Movies

An Android application that displays upcoming movies fetched from [The Movie Database (TMDB) API](https://www.themoviedb.org/), with offline support via local caching, a detail screen for each movie, and a favorites system.

<video src="https://github.com/user-attachments/assets/5a7d3460-f1fc-4aae-9408-c1080657cb04"></video>

---

## Purpose

The app allows users to:
- Browse a list of upcoming theatrical releases sorted by release date and rating.
- See the movie poster, title, release date, and rating at a glance.
- Identify unreleased movies with a dynamic countdown ("Release in X days").
- Tap a movie to open a detail screen with backdrop, tagline, genres, overview, runtime, and status.
- Heart a movie from the detail screen — hearted movies float to the top of the list with a visible heart indicator.
- Use the app offline — the list is cached locally and served from the database while a background refresh runs.

---

## Tech Stack

**Language & Build**
Kotlin 2.1 · AGP 9 · KSP · Gradle version catalogs

**UI**
Jetpack Compose · Material 3 · Navigation Compose · Coil 2

**Architecture**
Jetpack ViewModel · StateFlow · Coroutines · Flow

**Dependency Injection**
Koin 4

**Networking**
Retrofit 2 · OkHttp 4 · Gson

**Persistence**
Room 2

**Testing**
JUnit 4 · MockK · kotlinx-coroutines-test · Turbine · Compose UI Test

---

## Architecture

The project follows Clean Architecture with a feature-first package structure, split into three layers:

```
Presentation  →  Domain  ←  Data
```

- **Domain** — plain Kotlin. Models, repository interfaces, use cases. No Android dependencies.
- **Data** — implements the domain interfaces. Room DAOs, Retrofit services, mappers.
- **Presentation** — Compose UI, ViewModels. Observes state, dispatches actions.

The dependency rule flows inward: Presentation and Data both depend on Domain, but never on each other.

---

## Features

### Movie list
Movies are fetched from the TMDB API and cached in a local Room database. The list is always served from the database; a background refresh runs on every launch and on manual retry. Each item shows the poster, title, release date, and either a star rating or a release countdown for unreleased movies.

### Heart / Favorites
Heart state is stored in a separate `hearted_movies` Room table (movie IDs only), isolated from the movie cache so it survives schema migrations and network refreshes.

- **Toggle from the detail screen** — a heart button sits in the top-right corner of the backdrop image. It is white when not hearted, pink when hearted.
- **Visible in the list** — each list item shows a small heart icon as a read-only indicator. Hearted movies are automatically sorted to the top.
- **Reactive** — both screens observe the same `HeartRepository` flow. Toggling from detail is immediately reflected in the list without any manual refresh.

```
HeartDao ──► HeartRepositoryImpl ──► ObserveHeartedIdsUseCase ──► MovieListViewModel (sort)
                                  └──► ToggleHeartUseCase     ──► MovieDetailViewModel (toggle)
```

---

## Component pattern

Every UI component follows a four-part pattern:

- **`XxxParams`** — public data class holding all inputs (domain models or primitives).
- **`XxxComponent`** — public entry point. Owns all mapping logic: constructs URLs, resolves `stringResource`, formats values. Delegates to content.
- **`XxxComponentContent`** — private composable. Receives only flat primitives and strings — no domain models, no resource lookups.
- **`XxxPreviewProvider`** — `PreviewParameterProvider<XxxParams>` with representative sample data.

Each component lives in its own sub-package under `presentation/components/`:

```
components/
├── error/
│   ├── ErrorComponent.kt
│   └── ErrorComponentPreviewProvider.kt
├── loading/
│   └── LoadingComponent.kt
├── movieitem/
│   ├── MovieItemComponent.kt       # resolves MovieStatus (Rated / ReleaseStatus), heart indicator
│   └── MovieItemPreviewProvider.kt
└── movielist/
    ├── MovieListComponent.kt
    └── MovieListPreviewProvider.kt
```

---

## Testing

### Unit tests — `src/test/`

Dependencies: **JUnit 4 · MockK · kotlinx-coroutines-test · Turbine**

| File | What it covers |
|---|---|
| `DateExtensionsTest` | `formatToDefaultDate`, `daysUntilRelease` — valid input, invalid format, past/future |
| `MovieMapperTest` | `MovieDto → MovieEntity → Movie` round-trip, null poster path, `voteCount` mapping |
| `MovieDetailMapperTest` | `MovieDetailDto → MovieDetail`, genres list, null runtime/paths |
| `TmdbImageConfigTest` | URL constants, `posterSmallUrl` / `posterLargeUrl` / `backdropUrl` with null and non-null paths |
| `NetworkModuleTest` | Auth interceptor adds `Authorization` and `Accept` headers, preserves URL/method, interceptor count per build type, Retrofit base URL, shared `OkHttpClient` instance |
| `ObserveMoviesUseCaseTest` | Delegates to repository, returns exact flow |
| `RefreshMoviesUseCaseTest` | Delegates to repository, propagates exception |
| `GetMovieDetailUseCaseTest` | Delegates with correct ID, propagates exception |
| `ObserveHeartedIdsUseCaseTest` | Delegates to `HeartRepository.observeHeartedIds` |
| `ToggleHeartUseCaseTest` | Delegates to `HeartRepository.toggleHeart` with correct ID |
| `MovieRepositoryImplTest` | Flow mapping, empty list, multi-entity, refresh upserts, `voteCount` assertion |
| `MovieDetailRepositoryImplTest` | Maps correctly, null runtime, service error |
| `HeartRepositoryImplTest` | `observeHeartedIds` maps list to `Set`, deduplicates; `toggleHeart` inserts when not hearted, deletes when hearted |
| `MovieListViewModelTest` | State transitions: Loading → Success / Empty / Error, error suppression on Success, RetryLoad, Refresh; hearted movies sorted first, heart removed restores order |
| `MovieDetailViewModelTest` | Loading → Success / Error, RetryLoad sequence, NavigateBack no-op; `isHearted` reflects flow, `ToggleHeart` action calls use case |
| `KoinModulesTest` | `networkModule`, `movieListModule`, `movieDetailModule` — all bindings resolve |

ViewModels are tested with `MainDispatcherRule` (replaces `Dispatchers.Main` with `StandardTestDispatcher`). `MutableSharedFlow` drives the movie stream; `MutableStateFlow<Set<Int>>(emptySet())` drives the heart stream so `combine` emits as soon as movies arrive.

### Instrumented tests — `src/androidTest/`

Dependencies: **Compose UI Test (ui-test-junit4 · ui-test-manifest)**

| File | What it covers |
|---|---|
| `ErrorComponentTest` | Message displayed, retry text, clicking retry triggers callback |
| `LoadingComponentTest` | Indeterminate progress indicator present |
| `MovieItemComponentTest` | Title/date/★/rating for rated movie; no ★ + "Already released" for past unrated; click triggers callback |
| `MovieListComponentTest` | All titles visible, click delivers correct movie ID, empty list |
| `MovieDetailGenresComponentTest` | Each genre chip rendered, single genre, empty list |
| `MovieDetailInfoComponentTest` | All info cell labels, rating value, N/A for unrated, runtime formatting (h/m and m-only), null runtime, date and status |
| `MovieDetailHeaderComponentTest` | Title, quoted tagline, blank tagline hidden, back button visible and clickable |
| `MovieDaoTest` | Empty DB, insert and observe, `ORDER BY releaseDate DESC` sort, `voteAverage DESC` tiebreak, `REPLACE` conflict strategy, batch insert, empty upsert |
| `HeartDaoTest` | Empty table, insert and observe, delete removes ID, `EXISTS` query, duplicate insert ignored (`IGNORE` strategy) |

`MovieDaoTest` and `HeartDaoTest` both use `Room.inMemoryDatabaseBuilder` with `allowMainThreadQueries()` to test real SQL behaviour.
