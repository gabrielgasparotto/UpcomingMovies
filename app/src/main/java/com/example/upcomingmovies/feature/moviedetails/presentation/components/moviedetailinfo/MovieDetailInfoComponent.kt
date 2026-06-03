package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailinfo

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.core.domain.formatToDefaultDate

private const val MINUTES_PER_HOUR = 60

internal data class MovieDetailInfoParams(
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int?,
    val releaseDate: String,
    val status: String,
)

private data class InfoCellData(
    val label: String,
    val value: String,
    val isHighlighted: Boolean = false,
)

@Composable
internal fun MovieDetailInfoComponent(params: MovieDetailInfoParams, modifier: Modifier = Modifier) {
    val hasRating = params.voteAverage != 0.0
    val runtimeText = params.runtime?.let {
        val hours = it / MINUTES_PER_HOUR
        val minutes = it % MINUTES_PER_HOUR
        if (hours > 0) stringResource(R.string.runtime_hours_minutes, hours, minutes)
        else stringResource(R.string.runtime_minutes, minutes)
    } ?: stringResource(R.string.not_available)
    MovieDetailInfoComponentContent(
        rating = InfoCellData(
            label = stringResource(R.string.label_rating),
            value = if (hasRating) stringResource(R.string.rating_value, params.voteAverage, params.voteCount)
                    else stringResource(R.string.not_available),
            isHighlighted = hasRating,
        ),
        runtime = InfoCellData(
            label = stringResource(R.string.label_runtime),
            value = runtimeText,
        ),
        releaseDate = InfoCellData(
            label = stringResource(R.string.label_release),
            value = params.releaseDate.formatToDefaultDate(),
        ),
        status = InfoCellData(
            label = stringResource(R.string.label_status),
            value = params.status,
        ),
        modifier = modifier,
    )
}

@Composable
private fun MovieDetailInfoComponentContent(
    rating: InfoCellData,
    runtime: InfoCellData,
    releaseDate: InfoCellData,
    status: InfoCellData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            InfoCell(data = rating)
            InfoCell(data = runtime)
            InfoCell(data = releaseDate)
            InfoCell(data = status)
        }
        HorizontalDivider()
    }
}

@Composable
private fun InfoCell(data: InfoCellData, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = data.label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = data.value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = if (data.isHighlighted) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@ComponentPreview
@Composable
private fun MovieDetailInfoComponentPreview(
    @PreviewParameter(MovieDetailInfoPreviewProvider::class) params: MovieDetailInfoParams,
) {
    MaterialTheme {
        MovieDetailInfoComponent(params = params)
    }
}
