package com.example.upcomingmovies.feature.movielist.presentation.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.upcomingmovies.feature.core.domain.ComponentPreview

@Composable
internal fun LoadingComponent(modifier: Modifier = Modifier) {
    LoadingComponentContent(modifier = modifier)
}

@Composable
private fun LoadingComponentContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@ComponentPreview
@Composable
private fun LoadingComponentPreview() {
    MaterialTheme {
        LoadingComponent()
    }
}
