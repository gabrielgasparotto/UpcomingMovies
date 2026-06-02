package com.example.upcomingmovies.feature.moviedetails.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.moviedetails.presentation.components.sampleMovieDetail
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailState

class MovieDetailStatePreviewProvider : PreviewParameterProvider<MovieDetailState> {
    override val values = sequenceOf(
        MovieDetailState.Loading,
        MovieDetailState.Success(sampleMovieDetail),
        MovieDetailState.Error("Failed to load movie details."),
    )
}
