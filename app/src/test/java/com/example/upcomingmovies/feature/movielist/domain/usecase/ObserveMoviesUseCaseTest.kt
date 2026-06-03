package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

@Suppress("UnusedFlow")
class ObserveMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val useCase = ObserveMoviesUseCase(repository)

    @Test
    fun `invoke - delegates to repository observeMovies`() {
        // Given
        every { repository.observeMovies() } returns mockk()

        // When
        useCase()

        // Then
        verify(exactly = 1) { repository.observeMovies() }
    }
}
