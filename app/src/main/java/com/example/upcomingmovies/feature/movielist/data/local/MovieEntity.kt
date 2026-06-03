package com.example.upcomingmovies.feature.movielist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
)
