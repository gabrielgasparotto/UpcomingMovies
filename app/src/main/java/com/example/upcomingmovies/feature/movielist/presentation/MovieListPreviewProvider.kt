package com.example.upcomingmovies.feature.movielist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListState

internal val sampleMovies = listOf(
    Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", null, 7.0),
    Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", null, 6.5),
    Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", null, 7.2),
)

class MovieListStatePreviewProvider : PreviewParameterProvider<MovieListState> {
    override val values = sequenceOf(
        MovieListState.Loading,
        MovieListState.Success(sampleMovies),
        MovieListState.Success(emptyList()),
        MovieListState.Error("Unable to load movies. Check your connection."),
    )
}
