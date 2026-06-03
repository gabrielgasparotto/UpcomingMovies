package com.example.upcomingmovies.feature.movielist.presentation.components.error

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class ErrorComponentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun errorComponent_displaysMessage() {
        // Given
        rule.setContent {
            MaterialTheme {
                ErrorComponent(
                    params = ErrorComponentParams(
                        message = "Something went wrong",
                        onRetryText = "Retry",
                        onRetry = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun errorComponent_displaysRetryButtonText() {
        // Given
        rule.setContent {
            MaterialTheme {
                ErrorComponent(
                    params = ErrorComponentParams(
                        message = "Error",
                        onRetryText = "Try again",
                        onRetry = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText("Try again").assertIsDisplayed()
    }

    @Test
    fun errorComponent_clickingRetry_invokesCallback() {
        // Given
        var retried = false
        rule.setContent {
            MaterialTheme {
                ErrorComponent(
                    params = ErrorComponentParams(
                        message = "Error",
                        onRetryText = "Retry",
                        onRetry = { retried = true },
                    )
                )
            }
        }

        // When
        rule.onNodeWithText("Retry").performClick()

        // Then
        rule.runOnIdle { assertTrue(retried) }
    }

    @Test
    fun errorComponent_differentMessages_eachDisplayedCorrectly() {
        // Given
        rule.setContent {
            MaterialTheme {
                ErrorComponent(
                    params = ErrorComponentParams(
                        message = "Failed to load movies.",
                        onRetryText = "Retry",
                        onRetry = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText("Failed to load movies.").assertIsDisplayed()
    }
}
