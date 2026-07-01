package com.example.upcomingmovies.feature.moviedetails.data.repository

import com.example.upcomingmovies.feature.core.domain.MoviesError
import com.example.upcomingmovies.feature.core.domain.MoviesException
import com.example.upcomingmovies.feature.moviedetails.data.remote.GenreDto
import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailDto
import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class MovieDetailRepositoryImplTest {

    private val service = mockk<MovieDetailService>()
    private val repository = MovieDetailRepositoryImpl(service)

    @Test
    fun `getMovieDetail - calls service and returns mapped domain model`() = runTest {
        // Given
        val movieId = 11
        val dto = buildMovieDetailDto(id = movieId)
        coEvery { service.getMovieDetail(movieId) } returns dto

        // When
        val result = repository.getMovieDetail(movieId)

        // Then
        coVerify(exactly = 1) { service.getMovieDetail(movieId) }
        assertEquals(dto.id, result.id)
        assertEquals(dto.title, result.title)
        assertEquals(dto.tagline, result.tagline)
        assertEquals(dto.overview, result.overview)
        assertEquals(dto.status, result.status)
        assertEquals(dto.releaseDate, result.releaseDate)
        assertEquals(dto.runtime, result.runtime)
        assertEquals(dto.voteAverage, result.voteAverage, 0.0)
        assertEquals(dto.voteCount, result.voteCount)
        assertEquals(listOf("Adventure", "Action"), result.genres)
    }

    @Test
    fun `getMovieDetail - null runtime - preserved in domain model`() = runTest {
        // Given
        val dto = buildMovieDetailDto(runtime = null)
        coEvery { service.getMovieDetail(any()) } returns dto

        // When
        val result = repository.getMovieDetail(1)

        // Then
        assertNull(result.runtime)
    }

    @Test
    fun `getMovieDetail - service throws IOException - wraps in MoviesException with NoNetwork`() = runTest {
        // Given
        coEvery { service.getMovieDetail(any()) } throws IOException()

        // When
        val thrown = runCatching { repository.getMovieDetail(99) }.exceptionOrNull()

        // Then
        assertTrue(thrown is MoviesException)
        assertEquals(MoviesError.NoNetwork, (thrown as MoviesException).error)
    }

    @Test
    fun `getMovieDetail - service throws HttpException 404 - wraps in MoviesException with NotFound`() = runTest {
        // Given
        val httpException = mockk<HttpException> { every { code() } returns 404 }
        coEvery { service.getMovieDetail(any()) } throws httpException

        // When
        val thrown = runCatching { repository.getMovieDetail(99) }.exceptionOrNull()

        // Then
        assertTrue(thrown is MoviesException)
        assertEquals(MoviesError.NotFound, (thrown as MoviesException).error)
    }

    @Test
    fun `getMovieDetail - service throws unknown exception - wraps in MoviesException with Unknown`() = runTest {
        // Given
        coEvery { service.getMovieDetail(any()) } throws RuntimeException("unexpected")

        // When
        val thrown = runCatching { repository.getMovieDetail(99) }.exceptionOrNull()

        // Then
        assertTrue(thrown is MoviesException)
        assertEquals(MoviesError.Unknown, (thrown as MoviesException).error)
    }
}

private fun buildMovieDetailDto(id: Int = 11, runtime: Int? = 121) = MovieDetailDto(
    id = id,
    title = "Star Wars",
    originalTitle = "Star Wars",
    tagline = "A long time ago in a galaxy far, far away...",
    overview = "Princess Leia is captured and held hostage.",
    status = "Released",
    releaseDate = "1977-05-25",
    runtime = runtime,
    voteAverage = 8.2,
    voteCount = 22061,
    popularity = 95.0,
    adult = false,
    video = false,
    posterPath = "/poster.jpg",
    backdropPath = "/backdrop.jpg",
    homepage = null,
    imdbId = "tt0076759",
    budget = 11000000L,
    revenue = 775398007L,
    originalLanguage = "en",
    genres = listOf(GenreDto(12, "Adventure"), GenreDto(28, "Action")),
    originCountry = listOf("US"),
)
