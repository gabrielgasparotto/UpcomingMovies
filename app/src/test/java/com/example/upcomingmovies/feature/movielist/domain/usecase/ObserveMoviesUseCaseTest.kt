package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val useCase = ObserveMoviesUseCase(repository)

    @Test
    fun `invoke - delegates to repository observeMovies`() {
        // Given
        val expectedFlow = flowOf(listOf(buildMovie()))
        every { repository.observeMovies() } returns expectedFlow

        // When
        val result = useCase()

        // Then
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.observeMovies() }
    }

    @Test
    fun `invoke - returns exact flow from repository`() {
        // Given
        val emptyFlow = flowOf(emptyList<Movie>())
        every { repository.observeMovies() } returns emptyFlow

        // When
        val result = useCase()

        // Then
        assertEquals(emptyFlow, result)
    }
}

private fun buildMovie() = Movie(
    id = 1,
    title = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = "/poster.jpg",
    voteAverage = 7.0,
    voteCount = 1234,
)
