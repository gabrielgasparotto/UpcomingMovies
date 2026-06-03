package com.example.upcomingmovies.feature.movielist.presentation.components.error

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class ErrorComponentPreviewProvider : PreviewParameterProvider<ErrorComponentParams> {
    override val values = sequenceOf(
        ErrorComponentParams(
            message = "No upcoming movies available.",
            onRetryText = "Retry",
            onRetry = {},
        ), ErrorComponentParams(
            message = "Failed to load movies.",
            onRetryText = "Retry",
            onRetry = {},
        )
    )
}

