package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleHeartUseCaseTest {

    private val repository = mockk<HeartRepository>()
    private val useCase = ToggleHeartUseCase(repository)

    @Test
    fun `invoke - delegates to repository toggleHeart with correct id`() = runTest {
        // Given
        coJustRun { repository.toggleHeart(any()) }

        // When
        useCase(movieId = 42)

        // Then
        coVerify(exactly = 1) { repository.toggleHeart(42) }
    }
}
