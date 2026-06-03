package com.example.upcomingmovies.feature.movielist.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview

@Composable
internal fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, modifier = Modifier.padding(bottom = 16.dp))
            Button(onClick = onRetry) { Text(stringResource(R.string.action_retry)) }
        }
    }
}

@ComponentPreview
@Composable
private fun ErrorContentPreview() {
    MaterialTheme {
        ErrorContent(
            message = "Failed to load movies.",
            onRetry = {},
        )
    }
}

@ComponentPreview
@Composable
private fun ErrorContentEmptyPreview() {
    MaterialTheme {
        ErrorContent(
            message = "No upcoming movies available.",
            onRetry = {},
        )
    }
}
