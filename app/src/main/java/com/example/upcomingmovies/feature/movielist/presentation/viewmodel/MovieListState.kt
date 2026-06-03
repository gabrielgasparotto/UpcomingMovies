package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import com.example.upcomingmovies.feature.movielist.domain.model.Movie

sealed class MovieListState {
    data object Loading : MovieListState()
    data object Empty : MovieListState()
    data class Success(
        val allMoviesTab: MovieListTabContent.Movies,
        val favoritesTab: MovieListTabContent,
    ) : MovieListState()
    data object Error : MovieListState()
}

sealed class MovieListTabContent {
    data class Movies(val movies: List<Movie>) : MovieListTabContent()
    data object Empty : MovieListTabContent()
}
