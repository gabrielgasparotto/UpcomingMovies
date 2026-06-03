package com.example.upcomingmovies.feature.movielist.presentation.components.movieitem

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.movielist.domain.model.Movie

internal class MovieItemPreviewProvider : PreviewParameterProvider<MovieItemParams> {
    override val values = sequenceOf(
        MovieItemParams(
            movie = Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", "/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg", 7.0),
            onClick = {},
        ),
        MovieItemParams(
            movie = Movie(2, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2099-01-01", "/jwMMQR69Xz9AYtX4u2uYJgfAAev.jpg", 0.0),
            onClick = {},
        ),
        MovieItemParams(
            movie = Movie(3, "Movie Without Poster", "No poster available.", "2023-05-17", null, 5.5),
            onClick = {},
        ),
    )
}
