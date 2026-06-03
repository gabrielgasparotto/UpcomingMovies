package com.example.upcomingmovies.feature.movielist.presentation.components.movielist

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieListComponentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun movieListComponent_displaysAllMovieTitles() {
        // Given
        val movies = buildMovieList()
        rule.setContent {
            MaterialTheme {
                MovieListComponent(
                    params = MovieListParams(movies = movies, onMovieClick = {})
                )
            }
        }

        // Then
        movies.forEach { movie ->
            rule.onNodeWithText(movie.title).assertIsDisplayed()
        }
    }

    @Test
    fun movieListComponent_clickingItem_invokesOnMovieClickWithCorrectId() {
        // Given
        var clickedId: Int? = null
        val movies = buildMovieList()
        rule.setContent {
            MaterialTheme {
                MovieListComponent(
                    params = MovieListParams(
                        movies = movies,
                        onMovieClick = { id -> clickedId = id },
                    )
                )
            }
        }

        // When
        rule.onNodeWithText("The Pope's Exorcist").performClick()

        // Then
        rule.runOnIdle { assertEquals(2, clickedId) }
    }

    @Test
    fun movieListComponent_emptyList_showsNothing() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListComponent(
                    params = MovieListParams(movies = emptyList(), onMovieClick = {})
                )
            }
        }

        // Then — no movie titles in the tree
        assertTrue(rule.onAllNodesWithText("Evil Dead Rise").fetchSemanticsNodes().isEmpty())
    }
}

private fun buildMovieList() = listOf(
    Movie(1, "Evil Dead Rise", "Two sisters find an ancient vinyl...", "2023-04-12", null, 7.0, 1234),
    Movie(2, "The Pope's Exorcist", "Father Gabriele Amorth investigates...", "2023-04-05", null, 6.5, 987),
    Movie(3, "Fast X", "Dom Toretto and his family face their deadliest foe.", "2023-05-17", null, 7.2, 5678),
)
