package com.example.upcomingmovies.feature.movielist.presentation.components.movielist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.presentation.components.movieitem.MovieItemComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movieitem.MovieItemParams

internal data class MovieListParams(
    val movies: List<Movie>,
    val onMovieClick: (Int) -> Unit,
)

@Composable
internal fun MovieListComponent(params: MovieListParams, modifier: Modifier = Modifier) {
    MovieListComponentContent(
        movies = params.movies,
        onMovieClick = params.onMovieClick,
        modifier = modifier,
    )
}

@Composable
private fun MovieListComponentContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieItemComponent(
                params = MovieItemParams(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                )
            )
            HorizontalDivider()
        }
    }
}

@ComponentPreview
@Composable
private fun MovieListComponentPreview(
    @PreviewParameter(MovieListPreviewProvider::class) params: MovieListParams,
) {
    MaterialTheme {
        MovieListComponent(params = params)
    }
}
