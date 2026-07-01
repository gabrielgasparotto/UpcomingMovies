package com.example.upcomingmovies.feature.movielist.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val isHearted: Boolean = false,
)
