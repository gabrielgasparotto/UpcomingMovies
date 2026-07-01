package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import kotlinx.collections.immutable.ImmutableList

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
    @Immutable
    data class Movies(val movies: ImmutableList<Movie>) : MovieListTabContent()
    data object Empty : MovieListTabContent()
}
