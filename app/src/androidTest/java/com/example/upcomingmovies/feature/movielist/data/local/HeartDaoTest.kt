package com.example.upcomingmovies.feature.movielist.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.upcomingmovies.feature.core.data.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class HeartDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: HeartDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.heartDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // region observeHeartedIds

    @Test
    fun observeHeartedIds_emptyTable_returnsEmptyList() = runBlocking {
        // When / Then
        assertTrue(dao.observeHeartedIds().first().isEmpty())
    }

    @Test
    fun observeHeartedIds_afterInsert_containsInsertedId() = runBlocking {
        // Given
        dao.insert(HeartedMovieEntity(movieId = 1))

        // When / Then
        assertEquals(listOf(1), dao.observeHeartedIds().first())
    }

    @Test
    fun observeHeartedIds_afterDelete_idIsRemoved() = runBlocking {
        // Given
        dao.insert(HeartedMovieEntity(movieId = 1))
        dao.insert(HeartedMovieEntity(movieId = 2))

        // When
        dao.delete(movieId = 1)

        // Then
        assertEquals(listOf(2), dao.observeHeartedIds().first())
    }

    // endregion

    // region exists

    @Test
    fun exists_entityNotPresent_returnsFalse() = runBlocking {
        // When / Then
        assertFalse(dao.exists(movieId = 99))
    }

    @Test
    fun exists_entityInserted_returnsTrue() = runBlocking {
        // Given
        dao.insert(HeartedMovieEntity(movieId = 5))

        // When / Then
        assertTrue(dao.exists(movieId = 5))
    }

    @Test
    fun exists_afterDelete_returnsFalse() = runBlocking {
        // Given
        dao.insert(HeartedMovieEntity(movieId = 5))
        dao.delete(movieId = 5)

        // When / Then
        assertFalse(dao.exists(movieId = 5))
    }

    // endregion

    // region insert

    @Test
    fun insert_duplicate_doesNotThrowAndKeepsSingleEntry() = runBlocking {
        // Given — IGNORE strategy should not throw on duplicate
        dao.insert(HeartedMovieEntity(movieId = 1))
        dao.insert(HeartedMovieEntity(movieId = 1))

        // When / Then
        assertEquals(1, dao.observeHeartedIds().first().size)
    }

    // endregion
}
