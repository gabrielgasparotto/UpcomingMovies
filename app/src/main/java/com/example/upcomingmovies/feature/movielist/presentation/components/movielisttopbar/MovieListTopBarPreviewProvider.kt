package com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class MovieListTopBarPreviewProvider : PreviewParameterProvider<MovieListTopBarParams> {
    override val values = sequenceOf(
        MovieListTopBarParams(selectedTabIndex = 0, onTabSelected = {}),
        MovieListTopBarParams(selectedTabIndex = 1, onTabSelected = {}),
    )
}
