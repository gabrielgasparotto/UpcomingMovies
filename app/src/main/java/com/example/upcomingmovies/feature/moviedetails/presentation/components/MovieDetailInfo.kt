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
import androidx.compose.ui.res.stringResource
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.core.domain.formatToDefaultDate

private const val MINUTES_PER_HOUR = 60

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
            val hasRating = voteAverage != 0.0
            InfoCell(
                label = stringResource(R.string.label_rating),
                value = if (hasRating) stringResource(R.string.rating_value, voteAverage, voteCount)
                        else stringResource(R.string.not_available),
                isHighlighted = hasRating,
            )
            InfoCell(
                label = stringResource(R.string.label_runtime),
                value = runtime?.let {
                    val hours = it / MINUTES_PER_HOUR
                    val minutes = it % MINUTES_PER_HOUR
                    if (hours > 0) stringResource(R.string.runtime_hours_minutes, hours, minutes)
                    else stringResource(R.string.runtime_minutes, minutes)
                } ?: stringResource(R.string.not_available),
            )
            InfoCell(label = stringResource(R.string.label_release), value = releaseDate.formatToDefaultDate())
            InfoCell(label = stringResource(R.string.label_status), value = status)
        }

        HorizontalDivider()
    }
}

@Composable
private fun InfoCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
) {
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
            color = if (isHighlighted) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface,
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
