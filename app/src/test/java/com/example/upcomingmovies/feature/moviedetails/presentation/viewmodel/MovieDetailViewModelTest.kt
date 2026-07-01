package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

import app.cash.turbine.test
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.usecase.GetMovieDetailUseCase
import kotlinx.collections.immutable.persistentListOf
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ToggleHeartUseCase
import com.example.upcomingmovies.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMovieDetailUseCase = mockk<GetMovieDetailUseCase>()
    private val observeHeartedIdsUseCase = mockk<ObserveHeartedIdsUseCase>()
    private val toggleHeartUseCase = mockk<ToggleHeartUseCase>()
    private val heartIdsFlow = MutableStateFlow<Set<Int>>(emptySet())
    private val movieId = 11

    @Before
    fun setUp() {
        every { observeHeartedIdsUseCase() } returns heartIdsFlow
        coJustRun { toggleHeartUseCase(any()) }
    }

    private fun createViewModel() = MovieDetailViewModel(
        movieId,
        getMovieDetailUseCase,
        toggleHeartUseCase,
        observeHeartedIdsUseCase,
    )

    // region initial state

    @Test
    fun `initialState - is Loading`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()

        // When
        val viewModel = createViewModel()

        // Then
        assertEquals(MovieDetailState.Loading, viewModel.state.value)
    }

    // endregion

    // region loadDetail

    @Test
    fun `loadDetail - success - state becomes Success with movie`() = runTest {
        // Given
        val movieDetail = buildMovieDetail()
        coEvery { getMovieDetailUseCase(movieId) } returns movieDetail

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Success(movieDetail), viewModel.state.value)
    }

    @Test
    fun `loadDetail - failure - state becomes Error`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException("Not found")

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Error, viewModel.state.value)
    }

    @Test
    fun `loadDetail - failure with null message - state becomes Error`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException()

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Error, viewModel.state.value)
    }

    @Test
    fun `loadDetail - calls use case with correct movieId`() = runTest {
        // Given
        val specificId = 99
        coEvery { getMovieDetailUseCase(specificId) } returns buildMovieDetail(id = specificId)
        MovieDetailViewModel(specificId, getMovieDetailUseCase, toggleHeartUseCase, observeHeartedIdsUseCase)

        // When
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { getMovieDetailUseCase(specificId) }
    }

    // endregion

    // region retryLoad

    @Test
    fun `retryLoad - transitions through Loading then reaches Success`() = runTest {
        // Given — reach Error state first
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException("error")
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When — retry with a successful use case
        val movieDetail = buildMovieDetail()
        coEvery { getMovieDetailUseCase(movieId) } returns movieDetail

        // Then — observe Loading → Success via Turbine
        viewModel.state.test {
            assertEquals(MovieDetailState.Error, awaitItem())
            viewModel.onAction(MovieDetailAction.RetryLoad)
            advanceUntilIdle()
            assertEquals(MovieDetailState.Loading, awaitItem())
            assertEquals(MovieDetailState.Success(movieDetail), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retryLoad - success - state becomes Success`() = runTest {
        // Given — reach Error state
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException("error")
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When retry with success
        val movieDetail = buildMovieDetail()
        coEvery { getMovieDetailUseCase(movieId) } returns movieDetail
        viewModel.onAction(MovieDetailAction.RetryLoad)
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Success(movieDetail), viewModel.state.value)
    }

    @Test
    fun `retryLoad - calls use case again`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException("error")
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()
        viewModel.onAction(MovieDetailAction.RetryLoad)
        advanceUntilIdle()

        // Then — use case was called twice: once in init, once on retry
        coVerify(exactly = 2) { getMovieDetailUseCase(movieId) }
    }

    // endregion

    // region navigateBack

    @Test
    fun `navigateBack - does not change state`() = runTest {
        // Given
        val movieDetail = buildMovieDetail()
        coEvery { getMovieDetailUseCase(movieId) } returns movieDetail
        val viewModel = createViewModel()
        advanceUntilIdle()
        val stateBefore = viewModel.state.value

        // When
        viewModel.onAction(MovieDetailAction.NavigateBack)

        // Then
        assertEquals(stateBefore, viewModel.state.value)
        assertTrue(viewModel.state.value is MovieDetailState.Success)
    }

    // endregion

    // region heart feature

    @Test
    fun `isHearted - initially false when movieId not in hearted ids`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isHearted.value)
    }

    @Test
    fun `isHearted - true when movieId is in hearted set`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        heartIdsFlow.value = setOf(movieId)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isHearted.value)
    }

    @Test
    fun `isHearted - false when movieId removed from hearted set`() = runTest {
        // Given — start hearted
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()
        heartIdsFlow.value = setOf(movieId)
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.isHearted.value)

        // When heart is removed
        heartIdsFlow.value = emptySet()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isHearted.value)
    }

    @Test
    fun `toggleHeart - action dispatched - calls toggleHeartUseCase with movieId`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } returns buildMovieDetail()
        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onAction(MovieDetailAction.ToggleHeart)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleHeartUseCase(movieId) }
    }

    // endregion
}

private fun buildMovieDetail(id: Int = 11) = MovieDetail(
    id = id,
    title = "Star Wars",
    tagline = "A long time ago in a galaxy far, far away...",
    overview = "Princess Leia is captured and held hostage.",
    status = "Released",
    releaseDate = "1977-05-25",
    runtime = 121,
    voteAverage = 8.2,
    voteCount = 22061,
    posterPath = "/poster.jpg",
    backdropPath = "/backdrop.jpg",
    genres = persistentListOf("Adventure", "Action"),
)
