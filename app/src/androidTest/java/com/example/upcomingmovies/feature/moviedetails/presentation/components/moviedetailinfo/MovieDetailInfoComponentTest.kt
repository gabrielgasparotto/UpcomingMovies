package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailinfo

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.upcomingmovies.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieDetailInfoComponentTest {

    @get:Rule
    val rule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun movieDetailInfoComponent_displaysAllLabels() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams())
            }
        }

        // Then — all four info cell labels are visible
        rule.onNodeWithText(context.getString(R.string.label_rating)).assertIsDisplayed()
        rule.onNodeWithText(context.getString(R.string.label_runtime)).assertIsDisplayed()
        rule.onNodeWithText(context.getString(R.string.label_release)).assertIsDisplayed()
        rule.onNodeWithText(context.getString(R.string.label_status)).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_ratedMovie_displaysRatingValue() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams(voteAverage = 8.2, voteCount = 22061))
            }
        }

        // Then — rating string contains the star and value from the string resource
        val ratingText = context.getString(R.string.rating_value, 8.2, 22061)
        rule.onNodeWithText(ratingText).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_unratedMovie_displaysNotAvailable() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams(voteAverage = 0.0, voteCount = 0))
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.not_available)).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_runtimeInHoursAndMinutes_displayedCorrectly() {
        // Given — 121 minutes = 2h 1m
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams(runtime = 121))
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.runtime_hours_minutes, 2, 1)).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_runtimeInMinutesOnly_displayedCorrectly() {
        // Given — 45 minutes = 45m (no hours)
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams(runtime = 45))
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.runtime_minutes, 45)).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_nullRuntime_displaysNotAvailable() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(params = buildRatedParams(runtime = null))
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.not_available)).assertIsDisplayed()
    }

    @Test
    fun movieDetailInfoComponent_displaysFormattedReleaseDateAndStatus() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailInfoComponent(
                    params = buildRatedParams(releaseDate = "1977-05-25", status = "Released")
                )
            }
        }

        // Then — date formatted as dd/MM/yyyy, status shown as-is
        rule.onNodeWithText("25/05/1977").assertIsDisplayed()
        rule.onNodeWithText("Released").assertIsDisplayed()
    }
}

private fun buildRatedParams(
    voteAverage: Double = 8.2,
    voteCount: Int = 22061,
    runtime: Int? = 121,
    releaseDate: String = "1977-05-25",
    status: String = "Released",
) = MovieDetailInfoParams(
    voteAverage = voteAverage,
    voteCount = voteCount,
    runtime = runtime,
    releaseDate = releaseDate,
    status = status,
)
