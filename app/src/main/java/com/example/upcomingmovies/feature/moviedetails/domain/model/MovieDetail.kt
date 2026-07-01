package com.example.upcomingmovies.feature.moviedetails.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MovieDetail(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    val status: String,
    val releaseDate: String,
    val runtime: Int?,
    val voteAverage: Double,
    val voteCount: Int,
    val posterPath: String?,
    val backdropPath: String?,
    val genres: ImmutableList<String>,
)
