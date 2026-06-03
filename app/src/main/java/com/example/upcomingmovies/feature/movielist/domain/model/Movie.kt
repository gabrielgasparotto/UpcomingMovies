package com.example.upcomingmovies.feature.movielist.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
)
