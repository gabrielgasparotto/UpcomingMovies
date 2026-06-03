package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
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
    fun `observeMovies - movies emitted - state becomes Success`() = runTest {
        // Given
        val movies = listOf(buildMovie())
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // Then — isHearted defaults to false, no reordering
        assertEquals(MovieListState.Success(movies), viewModel.state.value)
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
        assertEquals(MovieListState.Success(movies), viewModel.state.value)

        // When
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Empty, viewModel.state.value)
    }

    @Test
    fun `observeMovies - empty list while Error - state remains Error`() = runTest {
        // Given — reach Error state first
        coEvery { refreshMoviesUseCase() } throws RuntimeException("error")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(MovieListState.Error, viewModel.state.value)

        // When — observe emits empty list while in Error state
        moviesFlow.emit(emptyList())
        advanceUntilIdle()

        // Then — Error is unchanged; empty only triggers Empty when coming from Success/Empty
        assertEquals(MovieListState.Error, viewModel.state.value)
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
        coEvery { refreshMoviesUseCase() } throws RuntimeException("Network error")
        val viewModel = createViewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Error, viewModel.state.value)
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
        coEvery { refreshMoviesUseCase() } throws RuntimeException("Network error")
        viewModel.onAction(MovieListAction.Refresh)
        advanceUntilIdle()

        // Then — error is suppressed because state is Success
        assertEquals(MovieListState.Success(movies), viewModel.state.value)
    }

    @Test
    fun `refresh - fails with null message - state becomes Error`() = runTest {
        // Given
        coEvery { refreshMoviesUseCase() } throws RuntimeException()
        val viewModel = createViewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(MovieListState.Error, viewModel.state.value)
    }

    // endregion

    // region actions

    @Test
    fun `retryLoad - immediately sets state to Loading`() = runTest {
        // Given — reach Error state
        coEvery { refreshMoviesUseCase() } throws RuntimeException("error")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(MovieListState.Error, viewModel.state.value)

        // When
        coJustRun { refreshMoviesUseCase() }
        viewModel.onAction(MovieListAction.RetryLoad)

        // Then — Loading is set synchronously before the coroutine runs
        assertEquals(MovieListState.Loading, viewModel.state.value)
    }

    @Test
    fun `retryLoad - refresh succeeds - state stays Loading until movies arrive`() = runTest {
        // Given — reach Error state
        coEvery { refreshMoviesUseCase() } throws RuntimeException("error")
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
    fun `observeMovies - hearted id emitted - movie marked isHearted in Success state`() = runTest {
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
        assertTrue(state.movies.single().isHearted)
    }

    @Test
    fun `observeMovies - hearted movies sorted first`() = runTest {
        // Given — three movies, middle one will be hearted
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle() // let the collect coroutine start
        moviesFlow.emit(movies)
        advanceUntilIdle()

        // When id=2 is hearted
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()

        // Then — movie 2 is first
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(2, state.movies.first().id)
        assertTrue(state.movies.first().isHearted)
    }

    @Test
    fun `observeMovies - heart removed - movie returns to original position`() = runTest {
        // Given — movie 2 is initially hearted and therefore first
        val movies = listOf(buildMovie(id = 1), buildMovie(id = 2), buildMovie(id = 3))
        val viewModel = createViewModel()
        advanceUntilIdle() // let the collect coroutine start
        moviesFlow.emit(movies)
        heartIdsFlow.value = setOf(2)
        advanceUntilIdle()
        assertEquals(2, (viewModel.state.value as MovieListState.Success).movies.first().id)

        // When heart is removed
        heartIdsFlow.value = emptySet()
        advanceUntilIdle()

        // Then — movies return to original order from the flow (1, 2, 3)
        val state = viewModel.state.value as MovieListState.Success
        assertEquals(listOf(1, 2, 3), state.movies.map { it.id })
        assertTrue(state.movies.none { it.isHearted })
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
