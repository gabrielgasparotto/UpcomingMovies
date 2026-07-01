package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

import androidx.annotation.StringRes
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail

sealed class MovieDetailState {
    data object Loading : MovieDetailState()
    data class Success(val movie: MovieDetail) : MovieDetailState()
    data class Error(@StringRes val messageRes: Int) : MovieDetailState()
}
