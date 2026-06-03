package com.example.upcomingmovies.feature.movielist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponentParams
import com.example.upcomingmovies.feature.movielist.presentation.components.loading.LoadingComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movielist.MovieListComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movielist.MovieListParams
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListAction
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListState
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoute(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: MovieListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieListScreen(
        state = state,
        onAction = viewModel::onAction,
        onMovieClick = onMovieClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.title_upcoming_movies)) }) },
        modifier = modifier,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state) {
                is MovieListState.Loading -> LoadingComponent()
                is MovieListState.Success -> MovieListComponent(
                    params = MovieListParams(
                        movies = state.movies,
                        onMovieClick = onMovieClick,
                    ),
                )
                is MovieListState.Error -> ErrorComponent(
                    params = ErrorComponentParams(
                        message = stringResource(R.string.error_load_movies),
                        onRetryText = stringResource(R.string.action_retry),
                        onRetry = { onAction(MovieListAction.RetryLoad) },
                    ),
                )
                is MovieListState.Empty -> ErrorComponent(
                    params = ErrorComponentParams(
                        message = stringResource(R.string.error_no_upcoming_movies),
                        onRetryText = stringResource(R.string.action_retry),
                        onRetry = { onAction(MovieListAction.RetryLoad) },
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListScreenPreview(
    @PreviewParameter(MovieListStatePreviewProvider::class) state: MovieListState,
) {
    MaterialTheme {
        MovieListScreen(state = state, onAction = {}, onMovieClick = {})
    }
}
