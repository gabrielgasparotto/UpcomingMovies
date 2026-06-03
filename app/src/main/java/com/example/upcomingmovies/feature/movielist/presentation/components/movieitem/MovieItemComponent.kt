package com.example.upcomingmovies.feature.movielist.presentation.components.movieitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.core.domain.daysUntilRelease
import com.example.upcomingmovies.feature.core.domain.formatToDefaultDate
import com.example.upcomingmovies.feature.movielist.domain.model.Movie

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"
private const val ONE_DAY = 1L
private const val TODAY = 0L

private sealed interface MovieStatus {
    data class Rated(val rating: String) : MovieStatus
    data class ReleaseStatus(val text: String) : MovieStatus
}

internal data class MovieItemParams(
    val movie: Movie,
    val onClick: () -> Unit,
)

@Composable
internal fun MovieItemComponent(params: MovieItemParams, modifier: Modifier = Modifier) {
    val movie = params.movie
    val status: MovieStatus = if (movie.voteAverage != 0.0) {
        MovieStatus.Rated("%.1f".format(movie.voteAverage))
    } else {
        MovieStatus.ReleaseStatus(
            when (val days = movie.releaseDate.daysUntilRelease()) {
                ONE_DAY -> stringResource(R.string.release_in_one_day)
                TODAY -> stringResource(R.string.releasing_today)
                else -> if (days > ONE_DAY) stringResource(R.string.release_in_days, days)
                        else stringResource(R.string.already_released)
            }
        )
    }
    MovieItemComponentContent(
        posterUrl = movie.posterPath?.let { "$POSTER_BASE_URL$it" },
        title = movie.title,
        releaseDate = movie.releaseDate.formatToDefaultDate(),
        status = status,
        onClick = params.onClick,
        modifier = modifier,
    )
}

@Composable
private fun MovieItemComponentContent(
    posterUrl: String?,
    title: String,
    releaseDate: String,
    status: MovieStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = releaseDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(6.dp))
            when (status) {
                is MovieStatus.Rated -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★",
                        color = Color(0xFFFFC107),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = status.rating,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                is MovieStatus.ReleaseStatus -> Text(
                    text = status.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MovieItemComponentPreview(
    @PreviewParameter(MovieItemPreviewProvider::class) params: MovieItemParams,
) {
    MaterialTheme {
        MovieItemComponent(params = params)
    }
}
