package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.MoviesError
import com.example.upcomingmovies.feature.core.domain.MoviesException
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
import kotlinx.collections.immutable.toImmutableList
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import com.example.upcomingmovies.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeMoviesUseCase = mockk<ObserveMoviesUseCase>()
    private val refreshMoviesUseCase = mockk<RefreshMoviesUseCase>()
    private val observeHeartedIdsUseCase = mockk<ObserveHeartedIdsUseCase>()

    private val moviesFlow = MutableSharedFlow<List<Movie>>()
    // StateFlow with initial value so combine emits as soon as moviesFlow emits
    private val heartIdsFlow = MutableStateFlow<Set<Int>>(emptySet())

    @Before
    fun setUp() {
        every { observeMoviesUseCase() } returns moviesFlow
        every { observeHeartedIdsUseCase() } returns heartIdsFlow
        coJustRun { refreshMoviesUseCase() }
    }

    private fun createViewModel() = MovieListViewModel(
        observeMoviesUseCase,
        refreshMoviesUseCase,
        observeHeartedIdsUseCase,
    )

    // region initial state

    @Test
    fun `initialState - is Loading`() = runTest {
        // Given / When
        val viewModel = createViewModel()

        // Then
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    // endregion

    // region observeMovies

    @Test
    fun `observeMovies - movies emitted - state becomes Success with correct allMoviesTab`() = runTest {
        // Given
        val movies = listOf(buildMovie())
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // Then — allMoviesTab contains all movies, favoritesTab is Empty (no hearted movies)
        val expected = MovieListState.Success(
            allMoviesTab = MovieListTabContent.Movies(movies.toImmutableList()),
            favoritesTab = MovieListTabContent.Empty,
        )
        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `observeMovies - empty list while Loading - state remains Loading`() = runTest {
        // Given
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then — Empty is only set when transitioning from Success/Empty
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    @Test
    fun `observeMovies - empty list after Success - state becomes Empty`() = runTest {
        // Given
        val movies = listOf(buildMovie())
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()
        val expected = MovieListState.Success(
            allMoviesTab = MovieListTabContent.Movies(movies.toImmutableList()),
            favoritesTab = MovieListTabContent.Empty,
        )
        assertEquals(expected, viewModel.state.value)

        // When
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Empty, viewModel.state.value)
    }

    @Test
    fun `observeMovies - empty list while Error - state remains Error`() = runTest {
        // Given — reach Error state first
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.Unknown)
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(MovieListState.Error(R.string.error_unknown), viewModel.state.value)

        // When — observe emits empty list while in Error state
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then — Error is unchanged; empty only triggers Empty when coming from Success/Empty
        assertEquals(MovieListState.Error(R.string.error_unknown), viewModel.state.value)
    }

    @Test
    fun `observeMovies - empty list while Empty - state remains Empty`() = runTest {
        // Given — reach Empty state first
        val movies = listOf(buildMovie())
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()
        moviesFlow.emit(emptyList())
        advanceUntilIdle()
        assertEquals(MovieListState.Empty, viewModel.state.value)

        // When
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Empty, viewModel.state.value)
    }

    // endregion

    // region refresh / error handling

    @Test
    fun `refresh - fails while Loading - state becomes Error`() = runTest {
        // Given
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.Unknown)
        val viewModel = createViewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Error(R.string.error_unknown), viewModel.state.value)
    }

    @Test
    fun `refresh - fails while Success - state remains Success`() = runTest {
        // Given
        val movies = listOf(buildMovie())
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // When refresh fails after Success
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.Unknown)
        viewModel.onAction(MovieListAction.Refresh)
        advanceUntilIdle()

        // Then — error is suppressed because state is Success
        val expected = MovieListState.Success(
            allMoviesTab = MovieListTabContent.Movies(movies.toImmutableList()),
            favoritesTab = MovieListTabContent.Empty,
        )
        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `refresh - fails with NoNetwork error - state becomes Error with no_network message`() = runTest {
        // Given
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.NoNetwork)
        val viewModel = createViewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Error(R.string.error_no_network), viewModel.state.value)
    }

    // endregion

    // region actions

    @Test
    fun `retryLoad - immediately sets state to Loading`() = runTest {
        // Given — reach Error state
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.Unknown)
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(MovieListState.Error(R.string.error_unknown), viewModel.state.value)

        // When
        coJustRun { refreshMoviesUseCase() }
        viewModel.onAction(MovieListAction.RetryLoad)

        // Then — Loading is set synchronously before the coroutine runs
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    @Test
    fun `retryLoad - refresh succeeds - state stays Loading until movies arrive`() = runTest {
        // Given — reach Error state
        coEvery { refreshMoviesUseCase() } throws MoviesException(MoviesError.Unknown)
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When retry with successful refresh
        coJustRun { refreshMoviesUseCase() }
        viewModel.onAction(MovieListAction.RetryLoad)
        advanceUntilIdle()

        // Then — still Loading (no movies emitted yet from observe)
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    @Test
    fun `refresh action - calls refresh use case`() = runTest {
        // Given
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onAction(MovieListAction.Refresh)
        advanceUntilIdle()

        // Then — no crash and state unchanged (refresh succeeded, no movies emitted)
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    // endregion

    // region heart feature

    @Test
    fun `observeMovies - hearted id emitted - movie marked isHearted in allMoviesTab`() = runTest {
        // Given
        val movie = buildMovie(id = 1)
        val viewModel = createViewModel()
        advanceUntilIdle() // let the collect coroutine start
        moviesFlow.emit(listOf(movie))
        advanceUntilIdle()

        // When heart flow emits id 1
        heartIdsFlow.value = setOf(1)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value as MovieListState.Success
        assertTrue(state.allMoviesTab.movies.single().isHearted)
    }

    @Test
    fun `observeMovies - hearted id emitted - favoritesTab contains only hearted movie`() = runTest {
        // Given — three movies, middle one will be hearted
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // When id=2 is hearted
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()

        // Then — favoritesTab contains only movie 2
        val state = viewModel.state.value as MovieListState.Success
        val favoritesContent = state.favoritesTab as MovieListTabContent.Movies
        assertEquals(listOf(2), favoritesContent.movies.map { it.id })
        assertTrue(favoritesContent.movies.single().isHearted)
    }

    @Test
    fun `observeMovies - original order preserved in allMoviesTab after hearting`() = runTest {
        // Given — three movies
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // When id=2 is hearted
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()

        // Then — allMoviesTab preserves original order (1, 2, 3)
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(listOf(1, 2, 3), state.allMoviesTab.movies.map { it.id })
    }

    @Test
    fun `observeMovies - no hearted movies - favoritesTab is Empty`() = runTest {
        // Given
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // Then — favoritesTab is Empty because no movies are hearted
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(MovieListTabContent.Empty, state.favoritesTab)
    }

    @Test
    fun `observeMovies - some hearted movies - favoritesTab is Movies with filtered list`() = runTest {
        // Given
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // When ids 1 and 3 are hearted
        heartIdsFlow.value = setOf(1, 3)
        advanceUntilIdle()

        // Then — favoritesTab contains only movies 1 and 3
        val state = viewModel.state.value as MovieListState.Success
        val favoritesContent = state.favoritesTab as MovieListTabContent.Movies
        assertEquals(listOf(1, 3), favoritesContent.movies.map { it.id })
        assertTrue(favoritesContent.movies.all { it.isHearted })
    }

    @Test
    fun `observeMovies - heart removed - favoritesTab becomes Empty`() = runTest {
        // Given — movie 2 is initially hearted
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()
        val stateAfterHeart = viewModel.state.value as MovieListState.Success
        assertTrue(stateAfterHeart.favoritesTab is MovieListTabContent.Movies)

        // When heart is removed
        heartIdsFlow.value = emptySet()
        advanceUntilIdle()

        // Then — favoritesTab becomes Empty
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(MovieListTabContent.Empty, state.favoritesTab)
    }

    @Test
    fun `observeMovies - heart removed - allMoviesTab restores original order`() = runTest {
        // Given — movie 2 is initially hearted
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle()
        moviesFlow.emit(movies)
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()

        // When heart is removed
        heartIdsFlow.value = emptySet()
        advanceUntilIdle()

        // Then — movies return to original order (1, 2, 3) and none are hearted
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(listOf(1, 2, 3), state.allMoviesTab.movies.map { it.id })
        assertTrue(state.allMoviesTab.movies.none { it.isHearted })
    }

    // endregion
}

private fun buildMovie(id: Int = 1) = Movie(
    id = id,
    title = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = "/poster.jpg",
    voteAverage = 7.0,
    voteCount = 1234,
)
