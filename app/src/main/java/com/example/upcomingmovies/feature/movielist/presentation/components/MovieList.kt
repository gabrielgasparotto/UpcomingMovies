package com.example.upcomingmovies.feature.movielist.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.core.extensions.ComponentPreview
import com.example.upcomingmovies.feature.movielist.domain.model.Movie

@Composable
internal fun MovieList(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
            HorizontalDivider()
        }
    }
}

@ComponentPreview
@Composable
private fun MovieListPreview() {
    MaterialTheme {
        MovieList(
            movies = listOf(
                Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", "/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg", 7.0),
                Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", "/9JBEPLTPSm0d1mbEcLxULjJq9Eh.jpg", 6.5),
                Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", "/jwMMQR69Xz9AYtX4u2uYJgfAAev.jpg", 7.2),
            ),
            onMovieClick = {},
        )
    }
}
