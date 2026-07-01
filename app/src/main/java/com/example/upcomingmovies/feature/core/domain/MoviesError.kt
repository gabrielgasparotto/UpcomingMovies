package com.example.upcomingmovies.feature.core.domain

sealed class MoviesError {
    data object NoNetwork : MoviesError()
    data object NotFound : MoviesError()
    data object ServerError : MoviesError()
    data object Unknown : MoviesError()
}
