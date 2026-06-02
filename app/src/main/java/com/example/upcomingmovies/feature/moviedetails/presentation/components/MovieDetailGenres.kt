package com.example.upcomingmovies.feature.moviedetails.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.core.extensions.ComponentPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MovieDetailGenres(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        genres.forEach { genre ->
            AssistChip(onClick = {}, label = { Text(genre) })
        }
    }
}

@ComponentPreview
@Composable
private fun MovieDetailGenresPreview() {
    MaterialTheme {
        MovieDetailGenres(genres = listOf("Adventure", "Action", "Science Fiction"))
    }
}
