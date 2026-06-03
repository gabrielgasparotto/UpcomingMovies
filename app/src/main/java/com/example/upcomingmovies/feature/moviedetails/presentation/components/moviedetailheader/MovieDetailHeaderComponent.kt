package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailheader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.core.presentation.TmdbImageConfig
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail

private val HeartedColor = Color(0xFFE91E63)

internal data class MovieDetailHeaderParams(
    val movie: MovieDetail,
    val isHearted: Boolean,
    val onBackClick: () -> Unit,
    val onHeartClick: () -> Unit,
)

@Composable
internal fun MovieDetailHeaderComponent(params: MovieDetailHeaderParams, modifier: Modifier = Modifier) {
    MovieDetailHeaderComponentContent(
        backdropUrl = TmdbImageConfig.backdropUrl(params.movie.backdropPath),
        posterUrl = TmdbImageConfig.posterLargeUrl(params.movie.posterPath),
        title = params.movie.title,
        tagline = params.movie.tagline.takeIf { it.isNotBlank() },
        isHearted = params.isHearted,
        onBackClick = params.onBackClick,
        onHeartClick = params.onHeartClick,
        modifier = modifier,
    )
}

@Composable
private fun MovieDetailHeaderComponentContent(
    backdropUrl: String?,
    posterUrl: String?,
    title: String,
    tagline: String?,
    isHearted: Boolean,
    onBackClick: () -> Unit,
    onHeartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 80f,
                    )
                )
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart).padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_back),
                    tint = Color.White,
                )
            }
            IconButton(
                onClick = onHeartClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = stringResource(
                        if (isHearted) R.string.cd_remove_from_favorites
                        else R.string.cd_add_to_favorites
                    ),
                    tint = if (isHearted) HeartedColor else Color.White.copy(alpha = 0.7f),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp)
                    .offset(y = (-30).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (tagline != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"$tagline\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MovieDetailHeaderComponentPreview(
    @PreviewParameter(MovieDetailHeaderPreviewProvider::class) params: MovieDetailHeaderParams,
) {
    MaterialTheme {
        MovieDetailHeaderComponent(params = params)
    }
}
