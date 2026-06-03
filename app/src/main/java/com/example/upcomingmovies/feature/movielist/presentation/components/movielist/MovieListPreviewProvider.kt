package com.example.upcomingmovies.feature.movielist.presentation.components.movielist

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.movielist.domain.model.Movie

internal class MovieListPreviewProvider : PreviewParameterProvider<MovieListParams> {
    override val values = sequenceOf(
        MovieListParams(
            movies = listOf(
                Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", "/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg", 7.0),
                Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", "/9JBEPLTPSm0d1mbEcLxULjJq9Eh.jpg", 6.5),
                Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", "/jwMMQR69Xz9AYtX4u2uYJgfAAev.jpg", 7.2),
            ),
            onMovieClick = {},
        )
    )
}
