package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailgenres

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class MovieDetailGenresPreviewProvider : PreviewParameterProvider<MovieDetailGenresParams> {
    override val values = sequenceOf(
        MovieDetailGenresParams(genres = listOf("Adventure", "Action", "Science Fiction")),
        MovieDetailGenresParams(genres = listOf("Drama")),
    )
}
