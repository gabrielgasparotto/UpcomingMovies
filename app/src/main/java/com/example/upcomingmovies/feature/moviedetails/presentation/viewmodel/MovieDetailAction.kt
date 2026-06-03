package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

sealed class MovieDetailAction {
    data object NavigateBack : MovieDetailAction()
    data object RetryLoad : MovieDetailAction()
    data object ToggleHeart : MovieDetailAction()
}
