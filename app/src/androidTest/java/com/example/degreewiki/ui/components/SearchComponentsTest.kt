package com.example.degreewiki.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchComponentsTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun activeFilterChipCanBeRemovedAndClearAllIsAvailable() {
        var removed: String? = null
        var cleared = false
        composeTestRule.setContent {
            DegreeWikiTheme {
                ActiveFilterChips(
                    filters = listOf("Finland", "Master's"),
                    onRemove = { removed = it },
                    onClearAll = { cleared = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Finland").performClick()
        composeTestRule.onNodeWithText("Clear all").performClick()
        composeTestRule.runOnIdle {
            assertEquals("Finland", removed)
            assertTrue(cleared)
        }
    }

    @Test fun searchEmptyStateShowsHelpfulActionsWithoutTechnicalCopy() {
        composeTestRule.setContent {
            DegreeWikiTheme {
                SearchEmptyState(
                    title = "No programs found",
                    message = "Try another keyword or remove some filters.",
                    hasQuery = true,
                    hasFilters = true,
                    onClearSearch = {},
                    onClearFilters = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No programs found").assertExists()
        composeTestRule.onNodeWithText("Clear search").assertExists()
        composeTestRule.onNodeWithText("Clear filters").assertExists()
        composeTestRule.onNodeWithText("cache", substring = true, ignoreCase = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("API", substring = true, ignoreCase = true).assertDoesNotExist()
    }
}
