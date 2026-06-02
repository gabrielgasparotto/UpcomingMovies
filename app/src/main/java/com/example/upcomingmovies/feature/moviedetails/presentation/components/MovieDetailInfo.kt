package com.example.upcomingmovies.feature.moviedetails.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.core.extensions.ComponentPreview
import com.example.upcomingmovies.feature.core.extensions.formatRuntime
import com.example.upcomingmovies.feature.core.extensions.formatToBrDate

@Composable
internal fun MovieDetailInfo(
    voteAverage: Double,
    voteCount: Int,
    runtime: Int?,
    releaseDate: String,
    status: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            InfoCell(label = "Rating", value = if (voteAverage == 0.0) "N/A" else "★ ${"%.1f".format(voteAverage)}\n(${voteCount})")
            InfoCell(label = "Runtime", value = runtime?.formatRuntime() ?: "N/A")
            InfoCell(label = "Release", value = releaseDate.formatToBrDate())
            InfoCell(label = "Status", value = status)
        }

        HorizontalDivider()
    }
}

@Composable
private fun InfoCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = if (label == "Rating" && value != "N/A") Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@ComponentPreview
@Composable
private fun MovieDetailInfoPreview() {
    MaterialTheme {
        MovieDetailInfo(
            voteAverage = 8.2,
            voteCount = 22061,
            runtime = 121,
            releaseDate = "1977-05-25",
            status = "Released",
        )
    }
}
