package com.example.upcomingmovies.feature.movielist.data.repository

import app.cash.turbine.test
import com.example.upcomingmovies.feature.movielist.data.local.HeartDao
import com.example.upcomingmovies.feature.movielist.data.local.HeartedMovieEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HeartRepositoryImplTest {

    private val dao = mockk<HeartDao>()
    private val repository = HeartRepositoryImpl(dao)

    // region observeHeartedIds

    @Test
    fun `observeHeartedIds - DAO emits list - returns as Set`() = runTest {
        // Given
        every { dao.observeHeartedIds() } returns flowOf(listOf(1, 2, 3))

        // When / Then
        repository.observeHeartedIds().test {
            assertEquals(setOf(1, 2, 3), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeHeartedIds - empty DAO - returns empty Set`() = runTest {
        // Given
        every { dao.observeHeartedIds() } returns flowOf(emptyList())

        // When / Then
        repository.observeHeartedIds().test {
            assertEquals(emptySet<Int>(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeHeartedIds - duplicate ids in DAO - deduplicated in Set`() = runTest {
        // Given
        every { dao.observeHeartedIds() } returns flowOf(listOf(1, 1, 2))

        // When / Then
        repository.observeHeartedIds().test {
            assertEquals(setOf(1, 2), awaitItem())
            awaitComplete()
        }
    }

    // endregion

    // region toggleHeart

    @Test
    fun `toggleHeart - movie not hearted - inserts entity`() = runTest {
        // Given
        coEvery { dao.exists(1) } returns false
        coJustRun { dao.insert(any()) }

        // When
        repository.toggleHeart(movieId = 1)

        // Then
        coVerify(exactly = 1) { dao.insert(HeartedMovieEntity(movieId = 1)) }
        coVerify(exactly = 0) { dao.delete(any()) }
    }

    @Test
    fun `toggleHeart - movie already hearted - deletes entity`() = runTest {
        // Given
        coEvery { dao.exists(1) } returns true
        coJustRun { dao.delete(any()) }

        // When
        repository.toggleHeart(movieId = 1)

        // Then
        coVerify(exactly = 1) { dao.delete(1) }
        coVerify(exactly = 0) { dao.insert(any()) }
    }

    // endregion
}
