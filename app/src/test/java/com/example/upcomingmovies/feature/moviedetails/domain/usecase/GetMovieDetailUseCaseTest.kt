package com.example.upcomingmovies.feature.moviedetails.domain.usecase

import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.repository.MovieDetailRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMovieDetailUseCaseTest {

    private val repository = mockk<MovieDetailRepository>()
    private val useCase = GetMovieDetailUseCase(repository)

    @Test
    fun `invoke - with movieId - delegates to repository and returns result`() = runTest {
        // Given
        val movieId = 42
        val expected = buildMovieDetail(id = movieId)
        coEvery { repository.getMovieDetail(movieId) } returns expected

        // When
        val result = useCase(movieId)

        // Then
        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getMovieDetail(movieId) }
    }

    @Test
    fun `invoke - propagates exception from repository`() = runTest {
        // Given
        val movieId = 1
        val error = RuntimeException("Not found")
        coEvery { repository.getMovieDetail(movieId) } throws error

        // When
        val thrown = runCatching { useCase(movieId) }.exceptionOrNull()

        // Then
        assertEquals(error, thrown)
    }
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
