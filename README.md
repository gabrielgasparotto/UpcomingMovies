# Upcoming Movies

An Android application that displays upcoming movies fetched from [The Movie Database (TMDB) API](https://www.themoviedb.org/), with offline support via local caching and a detail screen for each movie.

---

## Purpose

The app allows users to:
- Browse a list of upcoming theatrical releases sorted by release date and rating.
- See the movie poster, title, release date, and rating at a glance.
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
│   ├── MovieItemComponent.kt       # resolves MovieStatus (Rated / ReleaseStatus)
│   └── MovieItemPreviewProvider.kt
└── movielist/
    ├── MovieListComponent.kt
    └── MovieListPreviewProvider.kt
```

The `moviedetails` feature follows the same layout under its own `components/` folder (`moviedetailheader/`, `moviedetailinfo/`, `moviedetailgenres/`).

### MovieStatus

`MovieItemComponent` uses a private sealed interface to make the rating / release-status distinction compile-safe:

```kotlin
private sealed interface MovieStatus {
    data class Rated(val rating: String) : MovieStatus
    data class ReleaseStatus(val text: String) : MovieStatus
}
```

The `when` in the content function is exhaustive — adding a new subtype without handling it is a compile error.

---

## State

Each screen uses a `sealed class` for state and a `sealed class` for actions:

```kotlin
sealed class MovieListState {
    data object Loading : MovieListState()
    data object Empty   : MovieListState()
    data class  Success(val movies: List<Movie>) : MovieListState()
    data class  Error(val message: String? = null) : MovieListState()
}
```

`Empty` is emitted by the ViewModel (not the UI) when the observe flow returns an empty list after a prior `Success` or `Empty` state, keeping the UI free of list-size checks.

---

## Testing

### Unit tests — `src/test/`

Dependencies: **JUnit 4 · MockK · kotlinx-coroutines-test · Turbine**

| File | What it covers |
|---|---|
| `DateExtensionsTest` | `formatToDefaultDate`, `daysUntilRelease` — valid input, invalid format, past/future |
| `MovieMapperTest` | `MovieDto → MovieEntity → Movie` round-trip, null poster path |
| `MovieDetailMapperTest` | `MovieDetailDto → MovieDetail`, genres list, null runtime/paths |
| `ObserveMoviesUseCaseTest` | Delegates to repository, returns exact flow |
| `RefreshMoviesUseCaseTest` | Delegates to repository, propagates exception |
| `GetMovieDetailUseCaseTest` | Delegates with correct ID, propagates exception |
| `MovieRepositoryImplTest` | Flow mapping, empty list, multi-entity, refresh upserts, service error |
| `MovieDetailRepositoryImplTest` | Maps correctly, null runtime, service error |
| `MovieListViewModelTest` | All state transitions: Loading → Success / Empty / Error, error suppression on Success, RetryLoad, Refresh |
| `MovieDetailViewModelTest` | Loading → Success / Error, null message, RetryLoad sequence, NavigateBack no-op |

ViewModels are tested with `MainDispatcherRule` (replaces `Dispatchers.Main` with `StandardTestDispatcher`) and `MutableSharedFlow` to drive the observe stream.

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

`MovieDaoTest` uses `Room.inMemoryDatabaseBuilder` with `allowMainThreadQueries()` to test real SQL query behaviour including ordering and conflict resolution.
