package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

import app.cash.turbine.test
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.usecase.GetMovieDetailUseCase
import com.example.upcomingmovies.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMovieDetailUseCase = mockk<GetMovieDetailUseCase>()
    private val movieId = 11

    private fun createViewModel() = MovieDetailViewModel(movieId, getMovieDetailUseCase)

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
    fun `loadDetail - failure - state becomes Error with message`() = runTest {
        // Given
        val error = RuntimeException("Not found")
        coEvery { getMovieDetailUseCase(movieId) } throws error

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Error("Not found"), viewModel.state.value)
    }

    @Test
    fun `loadDetail - failure with null message - state becomes Error with null`() = runTest {
        // Given
        coEvery { getMovieDetailUseCase(movieId) } throws RuntimeException()

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailState.Error(null), viewModel.state.value)
    }

    @Test
    fun `loadDetail - calls use case with correct movieId`() = runTest {
        // Given
        val specificId = 99
        val detail = buildMovieDetail(id = specificId)
        coEvery { getMovieDetailUseCase(specificId) } returns detail

        // When
        val viewModel = MovieDetailViewModel(specificId, getMovieDetailUseCase)
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

        // Then — observe Loading → Success via Turbine since _state.value = Loading
        // is set inside the launched coroutine, not before it
        viewModel.state.test {
            assertEquals(MovieDetailState.Error("error"), awaitItem())
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
    genres = listOf("Adventure", "Action"),
)
