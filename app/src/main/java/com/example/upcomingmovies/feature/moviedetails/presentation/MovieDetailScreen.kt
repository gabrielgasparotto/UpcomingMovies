package com.example.upcomingmovies.feature.moviedetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar.HeaderGradientColors
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.upcomingmovies.R
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailgenres.MovieDetailGenresComponent
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailgenres.MovieDetailGenresParams
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailheader.MovieDetailHeaderComponent
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailheader.MovieDetailHeaderParams
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailinfo.MovieDetailInfoComponent
import com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailinfo.MovieDetailInfoParams
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailAction
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailState
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailViewModel
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponentParams
import com.example.upcomingmovies.feature.movielist.presentation.components.loading.LoadingComponent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailRoute(
    movieId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: MovieDetailViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isHearted by viewModel.isHearted.collectAsStateWithLifecycle()

    MovieDetailScreen(
        state = state,
        isHearted = isHearted,
        onAction = { action ->
            if (action == MovieDetailAction.NavigateBack) onBackClick()
            else viewModel.onAction(action)
        },
        modifier = modifier,
    )
}

@Composable
internal fun MovieDetailScreen(
    state: MovieDetailState,
    isHearted: Boolean,
    onAction: (MovieDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = HeaderGradientColors.first(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (state) {
                is MovieDetailState.Loading -> LoadingComponent()
                is MovieDetailState.Success -> MovieDetailContent(
                    movie = state.movie,
                    isHearted = isHearted,
                    onBackClick = { onAction(MovieDetailAction.NavigateBack) },
                    onHeartClick = { onAction(MovieDetailAction.ToggleHeart) },
                )
                is MovieDetailState.Error -> ErrorComponent(
                    params = ErrorComponentParams(
                        message = stringResource(state.messageRes),
                        onRetryText = stringResource(R.string.action_retry),
                        onRetry = { onAction(MovieDetailAction.RetryLoad) },
                    ),
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    isHearted: Boolean,
    onBackClick: () -> Unit,
    onHeartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            MovieDetailHeaderComponent(
                params = MovieDetailHeaderParams(
                    movie = movie,
                    isHearted = isHearted,
                    onBackClick = onBackClick,
                    onHeartClick = onHeartClick,
                ),
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            MovieDetailGenresComponent(
                params = MovieDetailGenresParams(genres = movie.genres),
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            MovieDetailInfoComponent(
                params = MovieDetailInfoParams(
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                    runtime = movie.runtime,
                    releaseDate = movie.releaseDate,
                    status = movie.status,
                ),
            )
        }
        if (movie.overview.isNotBlank()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.label_overview),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MovieDetailScreenPreview(
    @PreviewParameter(MovieDetailStatePreviewProvider::class) state: MovieDetailState,
) {
    MaterialTheme {
        MovieDetailScreen(state = state, isHearted = false, onAction = {})
    }
}
