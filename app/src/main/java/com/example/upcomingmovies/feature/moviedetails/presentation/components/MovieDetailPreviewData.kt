package com.example.upcomingmovies.feature.moviedetails.presentation.components

import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail

internal val sampleMovieDetail = MovieDetail(
    id = 11,
    title = "Star Wars",
    tagline = "A long time ago in a galaxy far, far away...",
    overview = "Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess.",
    status = "Released",
    releaseDate = "1977-05-25",
    runtime = 121,
    voteAverage = 8.2,
    voteCount = 22061,
    posterPath = "/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
    backdropPath = "/2w4xG178RpB4MDAIfTkqAuSJzec.jpg",
    genres = listOf("Adventure", "Action", "Science Fiction"),
)
