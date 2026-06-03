package com.example.upcomingmovies.feature.moviedetails.presentation

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.upcomingmovies.R
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.upcomingmovies.feature.core.domain.ComponentPreview
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.presentation.components.MovieDetailGenres
import com.example.upcomingmovies.feature.moviedetails.presentation.components.MovieDetailHeader
import com.example.upcomingmovies.feature.moviedetails.presentation.components.MovieDetailInfo
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailAction
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailState
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailViewModel
import com.example.upcomingmovies.feature.movielist.presentation.components.ErrorContent
import com.example.upcomingmovies.feature.movielist.presentation.components.LoadingContent
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

    MovieDetailScreen(
        state = state,
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
    onAction: (MovieDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        when (state) {
            is MovieDetailState.Loading -> LoadingContent(Modifier.padding(innerPadding))
            is MovieDetailState.Success -> MovieDetailContent(
                movie = state.movie,
                onBackClick = { onAction(MovieDetailAction.NavigateBack) },
                modifier = Modifier.padding(innerPadding),
            )
            is MovieDetailState.Error -> ErrorContent(
                message = state.message ?: stringResource(R.string.error_load_movie_details),
                onRetry = { onAction(MovieDetailAction.RetryLoad) },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            MovieDetailHeader(movie = movie, onBackClick = onBackClick)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            MovieDetailGenres(genres = movie.genres)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            MovieDetailInfo(
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
                runtime = movie.runtime,
                releaseDate = movie.releaseDate,
                status = movie.status,
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
        MovieDetailScreen(state = state, onAction = {})
    }
}
