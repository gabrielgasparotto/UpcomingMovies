package com.example.upcomingmovies.feature.movielist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.error.ErrorComponentParams
import com.example.upcomingmovies.feature.movielist.presentation.components.loading.LoadingComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movielist.MovieListComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movielist.MovieListParams
import com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar.MovieListTopBarComponent
import com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar.MovieListTopBarParams
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListAction
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListState
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListTabContent
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListViewModel
import kotlinx.coroutines.launch
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

@Composable
internal fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MovieListTopBarComponent(
                params = MovieListTopBarParams(
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
                ),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state) {
                is MovieListState.Success -> {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                    ) { page ->
                        when (val content = if (page == 0) state.allMoviesTab else state.favoritesTab) {
                            is MovieListTabContent.Movies -> MovieListComponent(
                                params = MovieListParams(
                                    movies = content.movies,
                                    onMovieClick = onMovieClick,
                                ),
                            )
                            is MovieListTabContent.Empty -> ErrorComponent(
                                params = ErrorComponentParams(
                                    message = stringResource(R.string.error_no_favorites),
                                    onRetryText = null,
                                    onRetry = null,
                                ),
                            )
                        }
                    }
                }
                is MovieListState.Loading -> LoadingComponent()
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
