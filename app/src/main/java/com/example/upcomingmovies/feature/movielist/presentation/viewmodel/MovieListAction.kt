package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

sealed class MovieListAction {
    data object Refresh : MovieListAction()
    data object RetryLoad : MovieListAction()
}
