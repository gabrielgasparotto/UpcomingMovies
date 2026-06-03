package com.example.upcomingmovies.feature.movielist.data.mapper

import com.example.upcomingmovies.feature.movielist.data.local.MovieEntity
import com.example.upcomingmovies.feature.movielist.data.remote.MovieDto
import com.example.upcomingmovies.feature.movielist.domain.model.Movie

internal fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    releaseDate = releaseDate,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
)

internal fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    releaseDate = releaseDate,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
)
