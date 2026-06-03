package com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.ComponentPreview

val HeaderGradientColors = listOf(Color(0xFF1A237E), Color(0xFF6A1B9A))
private val HeaderGradient = Brush.linearGradient(colors = HeaderGradientColors)
private val TabSelected = Color.White
private val TabUnselected = Color.White.copy(alpha = 0.55f)
private val TabIndicator = Color(0xFFE040FB)

internal data class MovieListTopBarParams(
    val selectedTabIndex: Int,
    val onTabSelected: (Int) -> Unit,
)

@Composable
internal fun MovieListTopBarComponent(params: MovieListTopBarParams, modifier: Modifier = Modifier) {
    MovieListTopBarComponentContent(
        selectedTabIndex = params.selectedTabIndex,
        onTabSelected = params.onTabSelected,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListTopBarComponentContent(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(HeaderGradient),
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(22.dp),
                    )
                    Text(
                        text = stringResource(R.string.title_upcoming_movies),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
            ),
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = TabSelected,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = TabIndicator,
                )
            },
            divider = {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.15f)),
                )
            },
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { onTabSelected(0) },
                text = {
                    Text(
                        text = stringResource(R.string.tab_all_movies),
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTabIndex == 0) TabSelected else TabUnselected,
                    )
                },
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) },
                text = {
                    Text(
                        text = stringResource(R.string.tab_favorites),
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTabIndex == 1) TabSelected else TabUnselected,
                    )
                },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun MovieListTopBarComponentPreview(
    @PreviewParameter(MovieListTopBarPreviewProvider::class) params: MovieListTopBarParams,
) {
    MaterialTheme {
        MovieListTopBarComponent(params = params)
    }
}
