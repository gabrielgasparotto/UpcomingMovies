package com.example.upcomingmovies.feature.moviedetails.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailState

class MovieDetailStatePreviewProvider : PreviewParameterProvider<MovieDetailState> {
    override val values = sequenceOf(
        MovieDetailState.Loading,
        MovieDetailState.Success(
            MovieDetail(
                id = 11,
                title = "Star Wars",
                tagline = "A long time ago in a galaxy far, far away...",
                overview = "Princess Leia is captured and held hostage by the evil Imperial forces.",
                status = "Released",
                releaseDate = "1977-05-25",
                runtime = 121,
                voteAverage = 8.2,
                voteCount = 22061,
                posterPath = "/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
                backdropPath = "/2w4xG178RpB4MDAIfTkqAuSJzec.jpg",
                genres = listOf("Adventure", "Action", "Science Fiction"),
            )
        ),
        MovieDetailState.Error,
    )
}
