package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailheader

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieDetailHeaderComponentTest {

    @get:Rule
    val rule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun movieDetailHeaderComponent_displaysTitle() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = {})
                )
            }
        }

        // Then
        rule.onNodeWithText("Star Wars").assertIsDisplayed()
    }

    @Test
    fun movieDetailHeaderComponent_withTagline_displaysTagline() {
        // Given
        val movie = buildMovieDetail(tagline = "A long time ago in a galaxy far, far away...")
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = movie,
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = {})
                )
            }
        }

        // Then — tagline is wrapped in quotes by the component
        rule.onNodeWithText("\"A long time ago in a galaxy far, far away...\"").assertIsDisplayed()
    }

    @Test
    fun movieDetailHeaderComponent_blankTagline_taglineNotShown() {
        // Given
        val movie = buildMovieDetail(tagline = "")
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = movie,
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = {})
                )
            }
        }

        // Then — no quoted empty string in the tree
        assertTrue(rule.onAllNodesWithText("\"\"").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun movieDetailHeaderComponent_backButton_displayed() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = {})
                )
            }
        }

        // Then
        rule.onNodeWithContentDescription(context.getString(R.string.cd_navigate_back))
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailHeaderComponent_clickingBackButton_invokesCallback() {
        // Given
        var backClicked = false
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = false,
                        onBackClick = { backClicked = true },
                        onHeartClick = {},
                    )
                )
            }
        }

        // When
        rule.onNodeWithContentDescription(context.getString(R.string.cd_navigate_back))
            .performClick()

        // Then
        rule.runOnIdle { assertTrue(backClicked) }
    }

    @Test
    fun movieDetailHeaderComponent_notHearted_heartButtonShowsAddDescription() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithContentDescription(context.getString(R.string.cd_add_to_favorites))
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailHeaderComponent_hearted_heartButtonShowsRemoveDescription() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = true,
                        onBackClick = {},
                        onHeartClick = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithContentDescription(context.getString(R.string.cd_remove_from_favorites))
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailHeaderComponent_clickingHeartButton_invokesCallback() {
        // Given
        var heartClicked = false
        rule.setContent {
            MaterialTheme {
                MovieDetailHeaderComponent(
                    params = MovieDetailHeaderParams(
                        movie = buildMovieDetail(),
                        isHearted = false,
                        onBackClick = {},
                        onHeartClick = { heartClicked = true },
                    )
                )
            }
        }

        // When
        rule.onNodeWithContentDescription(context.getString(R.string.cd_add_to_favorites))
            .performClick()

        // Then
        rule.runOnIdle { assertTrue(heartClicked) }
    }
}

private fun buildMovieDetail(tagline: String = "A long time ago in a galaxy far, far away...") =
    MovieDetail(
        id = 11,
        title = "Star Wars",
        tagline = tagline,
        overview = "Princess Leia is captured and held hostage.",
        status = "Released",
        releaseDate = "1977-05-25",
        runtime = 121,
        voteAverage = 8.2,
        voteCount = 22061,
        posterPath = null,
        backdropPath = null,
        genres = listOf("Adventure", "Action"),
    )
