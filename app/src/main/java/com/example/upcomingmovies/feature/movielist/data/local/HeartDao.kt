package com.example.upcomingmovies.feature.movielist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartDao {
    @Query("SELECT movieId FROM hearted_movies")
    fun observeHeartedIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: HeartedMovieEntity)

    @Query("DELETE FROM hearted_movies WHERE movieId = :movieId")
    suspend fun delete(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM hearted_movies WHERE movieId = :movieId)")
    suspend fun exists(movieId: Int): Boolean
}
