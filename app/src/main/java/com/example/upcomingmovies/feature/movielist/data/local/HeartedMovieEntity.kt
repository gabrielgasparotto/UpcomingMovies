package com.example.upcomingmovies.feature.movielist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hearted_movies")
data class HeartedMovieEntity(
    @PrimaryKey val movieId: Int,
)
