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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // region observeMovies

    @Test
    fun observeMovies_emptyDatabase_emitsEmptyList() = runBlocking {
        // Given — fresh in-memory database

        // When
        val result = dao.observeMovies().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun observeMovies_afterUpsert_emitsInsertedMovies() = runBlocking {
        // Given
        val movies = listOf(buildEntity(id = 1), buildEntity(id = 2))

        // When
        dao.upsertAll(movies)
        val result = dao.observeMovies().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.map { it.id }.containsAll(listOf(1, 2)))
    }

    @Test
    fun observeMovies_orderedByReleaseDateDescending() = runBlocking {
        // Given — three movies with different release dates
        val movies = listOf(
            buildEntity(id = 1, releaseDate = "2023-01-01"),
            buildEntity(id = 2, releaseDate = "2023-06-15"),
            buildEntity(id = 3, releaseDate = "2022-12-31"),
        )

        // When
        dao.upsertAll(movies)
        val result = dao.observeMovies().first()

        // Then — newest date first
        assertEquals(2, result[0].id) // 2023-06-15
        assertEquals(1, result[1].id) // 2023-01-01
        assertEquals(3, result[2].id) // 2022-12-31
    }

    @Test
    fun observeMovies_sameDateOrderedByVoteAverageDescending() = runBlocking {
        // Given — two movies with the same release date but different ratings
        val movies = listOf(
            buildEntity(id = 1, releaseDate = "2023-04-12", voteAverage = 6.0),
            buildEntity(id = 2, releaseDate = "2023-04-12", voteAverage = 8.5),
        )

        // When
        dao.upsertAll(movies)
        val result = dao.observeMovies().first()

        // Then — higher vote average first for the same date
        assertEquals(2, result[0].id) // 8.5
        assertEquals(1, result[1].id) // 6.0
    }

    @Test
    fun observeMovies_fullOrdering_releaseDateThenVoteAverage() = runBlocking {
        // Given — mixed dates and ratings
        val movies = listOf(
            buildEntity(id = 1, releaseDate = "2023-01-01", voteAverage = 6.0),
            buildEntity(id = 2, releaseDate = "2023-06-01", voteAverage = 7.0),
            buildEntity(id = 3, releaseDate = "2023-01-01", voteAverage = 8.0),
        )

        // When
        dao.upsertAll(movies)
        val result = dao.observeMovies().first()

        // Then — date DESC primary, vote DESC secondary
        assertEquals(2, result[0].id) // newest date
        assertEquals(3, result[1].id) // older date, higher rating
        assertEquals(1, result[2].id) // older date, lower rating
    }

    // endregion

    // region upsertAll

    @Test
    fun upsertAll_existingId_replacesMovie() = runBlocking {
        // Given
        dao.upsertAll(listOf(buildEntity(id = 1, title = "Original Title")))

        // When — upsert with same id but different data
        dao.upsertAll(listOf(buildEntity(id = 1, title = "Updated Title")))
        val result = dao.observeMovies().first()

        // Then — only one row, with updated data
        assertEquals(1, result.size)
        assertEquals("Updated Title", result[0].title)
    }

    @Test
    fun upsertAll_multipleMovies_allInserted() = runBlocking {
        // Given
        val movies = (1..5).map { buildEntity(id = it) }

        // When
        dao.upsertAll(movies)
        val result = dao.observeMovies().first()

        // Then
        assertEquals(5, result.size)
    }

    @Test
    fun upsertAll_emptyList_noRowsInserted() = runBlocking {
        // Given / When
        dao.upsertAll(emptyList())
        val result = dao.observeMovies().first()

        // Then
        assertTrue(result.isEmpty())
    }

    // endregion
}

private fun buildEntity(
    id: Int = 1,
    title: String = "Movie $id",
    releaseDate: String = "2023-01-01",
    voteAverage: Double = 7.0,
) = MovieEntity(
    id = id,
    title = title,
    overview = "Overview for movie $id",
    releaseDate = releaseDate,
    posterPath = null,
    voteAverage = voteAverage,
)
