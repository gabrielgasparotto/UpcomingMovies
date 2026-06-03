package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

@Suppress("UnusedFlow")
class ObserveHeartedIdsUseCaseTest {

    private val repository = mockk<HeartRepository>()
    private val useCase = ObserveHeartedIdsUseCase(repository)

    @Test
    fun `invoke - delegates to repository observeHeartedIds`() {
        // Given
        every { repository.observeHeartedIds() } returns mockk()

        // When
        useCase()

        // Then
        verify(exactly = 1) { repository.observeHeartedIds() }
    }
}
