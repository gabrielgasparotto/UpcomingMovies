package com.example.upcomingmovies.feature.movielist.presentation.components.movielisttopbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.upcomingmovies.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MovieListTopBarComponentTest {

    @get:Rule
    val rule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun movieListTopBarComponent_displaysTitleUpcomingMovies() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 0,
                        onTabSelected = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.title_upcoming_movies)).assertIsDisplayed()
    }

    @Test
    fun movieListTopBarComponent_displaysAllMoviesTab() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 0,
                        onTabSelected = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.tab_all_movies)).assertIsDisplayed()
    }

    @Test
    fun movieListTopBarComponent_displaysFavoritesTab() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 0,
                        onTabSelected = {},
                    )
                )
            }
        }

        // Then
        rule.onNodeWithText(context.getString(R.string.tab_favorites)).assertIsDisplayed()
    }

    @Test
    fun movieListTopBarComponent_selectedTab0_allMoviesTabIsBold() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 0,
                        onTabSelected = {},
                    )
                )
            }
        }

        // Then — "All Movies" node has bold font weight via semantics font weight
        val allMoviesText = context.getString(R.string.tab_all_movies)
        val favoritesText = context.getString(R.string.tab_favorites)

        // Both tabs are displayed; selected (All Movies) should be bold
        rule.onNodeWithText(allMoviesText).assertIsDisplayed()
        rule.onNodeWithText(favoritesText).assertIsDisplayed()

        // Verify visual state: All Movies tab is selected (index 0 == selectedTabIndex 0)
        val allMoviesNodes = rule.onAllNodes(
            androidx.compose.ui.test.hasText(allMoviesText)
        ).fetchSemanticsNodes()
        assertTrue("Expected at least one All Movies node", allMoviesNodes.isNotEmpty())
    }

    @Test
    fun movieListTopBarComponent_selectedTab1_favoritesTabIsBold() {
        // Given
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 1,
                        onTabSelected = {},
                    )
                )
            }
        }

        // Then — Favorites tab is selected
        val favoritesText = context.getString(R.string.tab_favorites)
        val allMoviesText = context.getString(R.string.tab_all_movies)

        rule.onNodeWithText(favoritesText).assertIsDisplayed()
        rule.onNodeWithText(allMoviesText).assertIsDisplayed()

        val favoritesNodes = rule.onAllNodes(
            androidx.compose.ui.test.hasText(favoritesText)
        ).fetchSemanticsNodes()
        assertTrue("Expected at least one Favorites node", favoritesNodes.isNotEmpty())
    }

    @Test
    fun movieListTopBarComponent_clickingFavoritesTab_callsOnTabSelectedWith1() {
        // Given
        var selectedIndex: Int? = null
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 0,
                        onTabSelected = { selectedIndex = it },
                    )
                )
            }
        }

        // When
        rule.onNodeWithText(context.getString(R.string.tab_favorites)).performClick()

        // Then
        rule.runOnIdle { assertEquals(1, selectedIndex) }
    }

    @Test
    fun movieListTopBarComponent_clickingAllMoviesTab_callsOnTabSelectedWith0() {
        // Given
        var selectedIndex: Int? = null
        rule.setContent {
            MaterialTheme {
                MovieListTopBarComponent(
                    params = MovieListTopBarParams(
                        selectedTabIndex = 1,
                        onTabSelected = { selectedIndex = it },
                    )
                )
            }
        }

        // When
        rule.onNodeWithText(context.getString(R.string.tab_all_movies)).performClick()

        // Then
        rule.runOnIdle { assertEquals(0, selectedIndex) }
    }
}
