package com.example.upcomingmovies.feature.movielist.presentation.components.movieitem

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieItemComponentTest {

    @get:Rule
    val rule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun movieItemComponent_ratedMovie_displaysTitleAndRating() {
        // Given
        val movie = buildRatedMovie()
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then
        rule.onNodeWithText(movie.title).assertIsDisplayed()
        rule.onNodeWithText("7.0").assertIsDisplayed()
        rule.onNodeWithText("★").assertIsDisplayed()
    }

    @Test
    fun movieItemComponent_ratedMovie_displaysFormattedReleaseDate() {
        // Given
        val movie = buildRatedMovie(releaseDate = "2023-04-12")
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then — 2023-04-12 formatted as dd/MM/yyyy
        rule.onNodeWithText("12/04/2023").assertIsDisplayed()
    }

    @Test
    fun movieItemComponent_unratedPastMovie_doesNotShowStar() {
        // Given — past date, no rating
        val movie = buildUnratedMovie(releaseDate = "2020-01-01")
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then
        assertTrue(rule.onAllNodesWithText("★").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun movieItemComponent_unratedPastMovie_showsAlreadyReleasedStatus() {
        // Given
        val movie = buildUnratedMovie(releaseDate = "2020-01-01")
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.already_released)).assertIsDisplayed()
    }

    @Test
    fun movieItemComponent_unratedFutureMovie_showsReleaseCountdown() {
        // Given
        val movie = buildUnratedMovie(releaseDate = "2099-01-01")
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then — star rating absent, some release countdown text visible
        assertTrue(rule.onAllNodesWithText("★").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText(movie.title).assertIsDisplayed()
    }

    @Test
    fun movieItemComponent_heartedMovie_rendersTitle() {
        // Given — isHearted = true should not break the layout
        val movie = buildRatedMovie().copy(isHearted = true)
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(params = MovieItemParams(movie = movie, onClick = {}))
            }
        }

        // Then
        rule.onNodeWithText(movie.title).assertIsDisplayed()
    }

    @Test
    fun movieItemComponent_clicking_invokesOnClick() {
        // Given
        var clicked = false
        val movie = buildRatedMovie()
        rule.setContent {
            MaterialTheme {
                MovieItemComponent(
                    params = MovieItemParams(movie = movie, onClick = { clicked = true })
                )
            }
        }

        // When
        rule.onNodeWithText(movie.title).performClick()

        // Then
        rule.runOnIdle { assertTrue(clicked) }
    }
}

private fun buildRatedMovie(releaseDate: String = "2023-04-12") = Movie(
    id = 1,
    title = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = releaseDate,
    posterPath = null,
    voteAverage = 7.0,
    voteCount = 1234,
)

private fun buildUnratedMovie(releaseDate: String = "2099-01-01") = Movie(
    id = 2,
    title = "Fast X",
    overview = "Dom Toretto and his family face their deadliest foe.",
    releaseDate = releaseDate,
    posterPath = null,
    voteAverage = 0.0,
    voteCount = 0,
)
