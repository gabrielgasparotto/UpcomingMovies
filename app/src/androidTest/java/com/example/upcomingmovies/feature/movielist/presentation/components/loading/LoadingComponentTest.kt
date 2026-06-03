package com.example.upcomingmovies.feature.movielist.presentation.components.loading

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoadingComponentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun loadingComponent_showsIndeterminateProgressIndicator() {
        // Given
        rule.setContent {
            MaterialTheme {
                LoadingComponent()
            }
        }

        // Then
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }
}
