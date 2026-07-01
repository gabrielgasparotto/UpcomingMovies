package com.example.upcomingmovies.feature.movielist.presentation.components.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.core.domain.ComponentPreview

@Stable
internal data class ErrorComponentParams(
    val message: String,
    val onRetryText: String?,
    val onRetry: (() -> Unit)?,
)

@Composable
internal fun ErrorComponent(params: ErrorComponentParams, modifier: Modifier = Modifier) {
    ErrorComponentContent(
        message = params.message,
        onRetry = params.onRetry,
        onRetryText = params.onRetryText,
        modifier = modifier,
    )
}

@Composable
private fun ErrorComponentContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetryText: String?,
    onRetry: (() -> Unit)?,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            if (onRetry != null && onRetryText != null) {
                Button(onClick = onRetry) { Text(onRetryText) }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ErrorComponentPreview(
    @PreviewParameter(ErrorComponentPreviewProvider::class) params: ErrorComponentParams
) {
    MaterialTheme {
        ErrorComponent(params = params)
    }
}
