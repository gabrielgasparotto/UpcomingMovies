package com.example.upcomingmovies.feature.moviedetails.data.mapper

import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailDto
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import kotlinx.collections.immutable.toImmutableList

internal fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title,
    tagline = tagline,
    overview = overview,
    status = status,
    releaseDate = releaseDate,
    runtime = runtime,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath,
    backdropPath = backdropPath,
    genres = genres.map { it.name }.toImmutableList(),
)
