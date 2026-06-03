package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RefreshMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val useCase = RefreshMoviesUseCase(repository)

    @Test
    fun `invoke - delegates to repository refresh`() = runTest {
        // Given
        coJustRun { repository.refresh() }

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.refresh() }
    }

    @Test
    fun `invoke - propagates exception from repository`() = runTest {
        // Given
        val error = RuntimeException("Network error")
        io.mockk.coEvery { repository.refresh() } throws error

        // When / Then
        val thrown = runCatching { useCase() }.exceptionOrNull()
        assertEquals(error, thrown)
    }

    private fun assertEquals(expected: Any?, actual: Any?) =
        org.junit.Assert.assertEquals(expected, actual)
}
