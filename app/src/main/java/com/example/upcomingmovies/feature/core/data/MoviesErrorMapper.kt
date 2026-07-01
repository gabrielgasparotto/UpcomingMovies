package com.example.upcomingmovies.feature.core.data

import com.example.upcomingmovies.feature.core.domain.MoviesError
import retrofit2.HttpException
import java.io.IOException

object MoviesErrorMapper {
    fun map(throwable: Throwable): MoviesError = when (throwable) {
        is IOException -> MoviesError.NoNetwork
        is HttpException -> when (throwable.code()) {
            404 -> MoviesError.NotFound
            in 500..599 -> MoviesError.ServerError
            else -> MoviesError.Unknown
        }
        else -> MoviesError.Unknown
    }
}
