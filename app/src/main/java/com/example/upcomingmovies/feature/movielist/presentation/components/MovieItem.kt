package com.example.upcomingmovies.feature.movielist.presentation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.upcomingmovies.feature.core.extensions.ComponentPreview
import com.example.upcomingmovies.feature.core.extensions.daysUntilRelease
import com.example.upcomingmovies.feature.core.extensions.formatToBrDate
import com.example.upcomingmovies.feature.core.extensions.toReleaseLabel
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"

@Composable
internal fun MovieItem(
    movie: Movie,
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
            model = movie.posterPath?.let { "$POSTER_BASE_URL$it" },
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.releaseDate.formatToBrDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (movie.voteAverage == 0.0) {
                Text(
                    text = movie.releaseDate.daysUntilRelease().toReleaseLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★",
                        color = Color(0xFFFFC107),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(movie.voteAverage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MovieItemRatedPreview() {
    MaterialTheme {
        MovieItem(
            movie = Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", "/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg", 7.0),
            onClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun MovieItemUnreleasedPreview() {
    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 30) }.time
    )
    MaterialTheme {
        MovieItem(
            movie = Movie(2, "Fast X", "Dom Toretto and his family face their deadliest foe.", dateStr, "/jwMMQR69Xz9AYtX4u2uYJgfAAev.jpg", 0.0),
            onClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun MovieItemNoPosterPreview() {
    MaterialTheme {
        MovieItem(
            movie = Movie(3, "Movie Without Poster", "No poster available.", "2023-05-17", null, 5.5),
            onClick = {},
        )
    }
}
