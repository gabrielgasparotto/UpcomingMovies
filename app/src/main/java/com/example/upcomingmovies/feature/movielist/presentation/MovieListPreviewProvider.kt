package com.example.upcomingmovies.feature.movielist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListState
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListTabContent

internal val sampleMovies = listOf(
    Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", null, 7.0, 1234),
    Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", null, 6.5, 987),
    Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", null, 7.2, 5678),
)

class MovieListStatePreviewProvider : PreviewParameterProvider<MovieListState> {
    override val values = sequenceOf(
        MovieListState.Loading,
        MovieListState.Success(
            allMoviesTab = MovieListTabContent.Movies(sampleMovies),
            favoritesTab = MovieListTabContent.Empty,
        ),
        MovieListState.Empty,
        MovieListState.Error,
    )
}
