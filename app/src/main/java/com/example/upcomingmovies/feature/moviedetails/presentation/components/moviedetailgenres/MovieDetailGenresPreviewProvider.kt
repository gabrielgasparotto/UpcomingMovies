package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailgenres

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf

internal class MovieDetailGenresPreviewProvider : PreviewParameterProvider<MovieDetailGenresParams> {
    override val values = sequenceOf(
        MovieDetailGenresParams(genres = persistentListOf("Adventure", "Action", "Science Fiction")),
        MovieDetailGenresParams(genres = persistentListOf("Drama")),
    )
}
