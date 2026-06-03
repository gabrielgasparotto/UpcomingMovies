package com.example.upcomingmovies.feature.movielist.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListAction
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListState
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListTabContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieListScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun movieListScreen_loadingState_showsLoadingIndicator() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Loading,
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // Then
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun movieListScreen_errorState_showsErrorMessageAndRetryButton() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Error,
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.error_load_movies)).assertIsDisplayed()
        rule.onNodeWithText(context.getString(R.string.action_retry)).assertIsDisplayed()
    }

    @Test
    fun movieListScreen_errorState_clickingRetry_invokesRetryLoadAction() {
        // Given
        var lastAction: MovieListAction? = null
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Error,
                    onAction = { lastAction = it },
                    onMovieClick = {},
                )
            }
        }

        // When
        rule.onNodeWithText(context.getString(R.string.action_retry)).performClick()

        // Then
        rule.runOnIdle { assertTrue(lastAction is MovieListAction.RetryLoad) }
    }

    @Test
    fun movieListScreen_emptyState_showsEmptyMessageAndRetryButton() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Empty,
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.error_no_upcoming_movies)).assertIsDisplayed()
        rule.onNodeWithText(context.getString(R.string.action_retry)).assertIsDisplayed()
    }

    @Test
    fun movieListScreen_successState_showsAllMoviesTitles() {
        // Given
        val movies = buildMovieList()
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Success(
                        allMoviesTab = MovieListTabContent.Movies(movies),
                        favoritesTab = MovieListTabContent.Empty,
                    ),
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // Then — page 0 is visible by default
        movies.forEach { movie ->
            rule.onNodeWithText(movie.title).assertIsDisplayed()
        }
    }

    @Test
    fun movieListScreen_successStateWithEmptyFavorites_swipeLeftShowsNoFavoritesMessage() {
        // Given
        val movies = buildMovieList()
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Success(
                        allMoviesTab = MovieListTabContent.Movies(movies),
                        favoritesTab = MovieListTabContent.Empty,
                    ),
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // When — swipe to page 1 (favorites)
        rule.onRoot().performTouchInput { swipeLeft() }
        rule.waitForIdle()

        // Then — no-favorites message shown, no retry button
        rule.onNodeWithText(context.getString(R.string.error_no_favorites)).assertIsDisplayed()
        assertTrue(
            rule.onAllNodesWithText(context.getString(R.string.action_retry))
                .fetchSemanticsNodes()
                .isEmpty()
        )
    }

    @Test
    fun movieListScreen_successStateWithFavoriteMovies_swipeLeftShowsFavoritedTitles() {
        // Given
        val favoritedMovie = Movie(
            id = 10,
            title = "Avatar: The Way of Water",
            overview = "Jake Sully and Ney'tiri form a family...",
            releaseDate = "2022-12-16",
            posterPath = null,
            voteAverage = 7.6,
            voteCount = 9876,
            isHearted = true,
        )
        val movies = buildMovieList()
        rule.setContent {
            MaterialTheme {
                MovieListScreen(
                    state = MovieListState.Success(
                        allMoviesTab = MovieListTabContent.Movies(movies),
                        favoritesTab = MovieListTabContent.Movies(listOf(favoritedMovie)),
                    ),
                    onAction = {},
                    onMovieClick = {},
                )
            }
        }

        // When — swipe to page 1 (favorites)
        rule.onRoot().performTouchInput { swipeLeft() }
        rule.waitForIdle()

        // Then — favorited movie title visible
        rule.onNodeWithText(favoritedMovie.title).assertIsDisplayed()
    }
}

private fun buildMovieList() = listOf(
    Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", null, 7.0, 1234),
    Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", null, 6.5, 987),
    Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", null, 7.2, 5678),
)
