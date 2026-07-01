package com.example.upcomingmovies.feature.core.presentation

import androidx.annotation.StringRes
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.MoviesError

object MoviesErrorMessageMapper {
    @StringRes
    fun map(error: MoviesError): Int = when (error) {
        MoviesError.NoNetwork -> R.string.error_no_network
        MoviesError.NotFound -> R.string.error_not_found
        MoviesError.ServerError -> R.string.error_server
        MoviesError.Unknown -> R.string.error_unknown
    }
}
