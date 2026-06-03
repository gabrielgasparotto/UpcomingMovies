package com.example.upcomingmovies.feature.moviedetails.presentation.components.moviedetailgenres

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieDetailGenresComponentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun movieDetailGenresComponent_displaysAllGenreChips() {
        // Given
        val genres = listOf("Adventure", "Action", "Science Fiction")
        rule.setContent {
            MaterialTheme {
                MovieDetailGenresComponent(params = MovieDetailGenresParams(genres = genres))
            }
        }

        // Then
        genres.forEach { genre ->
            rule.onNodeWithText(genre).assertIsDisplayed()
        }
    }

    @Test
    fun movieDetailGenresComponent_singleGenre_displayed() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailGenresComponent(params = MovieDetailGenresParams(genres = listOf("Drama")))
            }
        }

        // Then
        rule.onNodeWithText("Drama").assertIsDisplayed()
    }

    @Test
    fun movieDetailGenresComponent_emptyGenres_nothingDisplayed() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieDetailGenresComponent(params = MovieDetailGenresParams(genres = emptyList()))
            }
        }

        // Then — no genre chips in the tree
        rule.onNodeWithText("Adventure").assertDoesNotExist()
    }
}
