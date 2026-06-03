package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailinfo

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class MovieDetailInfoPreviewProvider : PreviewParameterProvider<MovieDetailInfoParams> {
    override val values = sequenceOf(
        MovieDetailInfoParams(
            voteAverage = 8.2,
            voteCount = 22061,
            runtime = 121,
            releaseDate = "1977-05-25",
            status = "Released",
        ),
        MovieDetailInfoParams(
            voteAverage = 0.0,
            voteCount = 0,
            runtime = null,
            releaseDate = "2025-12-01",
            status = "Upcoming",
        ),
    )
}
