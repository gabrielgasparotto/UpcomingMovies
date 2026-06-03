package com.example.upcomingmovies.feature.movielist.data.repository

import app.cash.turbine.test
import com.example.upcomingmovies.feature.movielist.data.local.MovieDao
import com.example.upcomingmovies.feature.movielist.data.local.MovieEntity
import com.example.upcomingmovies.feature.movielist.data.remote.MovieDto
import com.example.upcomingmovies.feature.movielist.data.remote.MovieService
import com.example.upcomingmovies.feature.movielist.data.remote.UpcomingMoviesResponse
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieRepositoryImplTest {

    private val dao = mockk<MovieDao>()
    private val service = mockk<MovieService>()
    private val repository = MovieRepositoryImpl(dao, service)

    // region observeMovies

    @Test
    fun `observeMovies - dao emits entities - emits mapped domain models`() = runTest {
        // Given
        val entity = buildMovieEntity()
        every { dao.observeMovies() } returns flowOf(listOf(entity))

        // When / Then
        repository.observeMovies().test {
            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals(entity.id, movies[0].id)
            assertEquals(entity.title, movies[0].title)
            assertEquals(entity.overview, movies[0].overview)
            assertEquals(entity.releaseDate, movies[0].releaseDate)
            assertEquals(entity.posterPath, movies[0].posterPath)
            assertEquals(entity.voteAverage, movies[0].voteAverage, 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `observeMovies - dao emits empty list - emits empty list`() = runTest {
        // Given
        every { dao.observeMovies() } returns flowOf(emptyList())

        // When / Then
        repository.observeMovies().test {
            assertEquals(emptyList<Movie>(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeMovies - multiple entities - all mapped correctly`() = runTest {
        // Given
        val entities = listOf(buildMovieEntity(id = 1), buildMovieEntity(id = 2))
        every { dao.observeMovies() } returns flowOf(entities)

        // When / Then
        repository.observeMovies().test {
            val movies = awaitItem()
            assertEquals(2, movies.size)
            assertEquals(1, movies[0].id)
            assertEquals(2, movies[1].id)
            awaitComplete()
        }
    }

    // endregion

    // region refresh

    @Test
    fun `refresh - fetches from service and upserts mapped entities`() = runTest {
        // Given
        val dto = buildMovieDto()
        val response = UpcomingMoviesResponse(page = 1, results = listOf(dto), totalPages = 1, totalResults = 1)
        io.mockk.coEvery { service.getUpcomingMovies() } returns response
        coJustRun { dao.upsertAll(any()) }
        val capturedEntities = slot<List<MovieEntity>>()

        // When
        repository.refresh()

        // Then
        coVerify { dao.upsertAll(capture(capturedEntities)) }
        val entity = capturedEntities.captured.single()
        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.overview, entity.overview)
        assertEquals(dto.releaseDate, entity.releaseDate)
        assertEquals(dto.posterPath, entity.posterPath)
        assertEquals(dto.voteAverage, entity.voteAverage, 0.0)
        assertEquals(dto.voteCount, entity.voteCount)
    }

    @Test
    fun `refresh - empty results - upserts empty list`() = runTest {
        // Given
        val response = UpcomingMoviesResponse(page = 1, results = emptyList(), totalPages = 0, totalResults = 0)
        io.mockk.coEvery { service.getUpcomingMovies() } returns response
        coJustRun { dao.upsertAll(any()) }

        // When
        repository.refresh()

        // Then
        coVerify { dao.upsertAll(emptyList()) }
    }

    @Test
    fun `refresh - service throws - propagates exception`() = runTest {
        // Given
        val error = RuntimeException("Network error")
        io.mockk.coEvery { service.getUpcomingMovies() } throws error

        // When
        val thrown = runCatching { repository.refresh() }.exceptionOrNull()

        // Then
        assertEquals(error, thrown)
    }

    // endregion
}

private fun buildMovieEntity(id: Int = 1) = MovieEntity(
    id = id,
    title = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = "/poster.jpg",
    voteAverage = 7.0,
    voteCount = 1234,
)

private fun buildMovieDto(id: Int = 1) = MovieDto(
    id = id,
    title = "Evil Dead Rise",
    originalTitle = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = "/poster.jpg",
    backdropPath = "/backdrop.jpg",
    voteAverage = 7.0,
    voteCount = 1234,
    popularity = 80.5,
    genreIds = listOf(27),
    adult = false,
    video = false,
    originalLanguage = "en",
)
