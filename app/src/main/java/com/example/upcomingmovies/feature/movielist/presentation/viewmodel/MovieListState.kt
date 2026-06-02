package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import com.example.upcomingmovies.feature.movielist.domain.model.Movie

sealed class MovieListState {
    data object Loading : MovieListState()
    data class Success(val movies: List<Movie>) : MovieListState()
    data class Error(val message: String) : MovieListState()
}
