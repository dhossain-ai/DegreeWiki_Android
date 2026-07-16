package com.example.degreewiki.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.degreewiki.ui.features.profile.SavedProgramsContent
import org.junit.Rule
import org.junit.Test

class SavedProgramComponentsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loggedOutSaveShowsLoginPrompt() {
        composeRule.setContent {
            MaterialTheme {
                LoginToSavePrompt(onDismiss = {}, onLogIn = {})
            }
        }

        composeRule.onNodeWithText("Log in to save programs").assertIsDisplayed()
        composeRule.onNodeWithText(
            "Log in to save programs and access them across devices."
        ).assertIsDisplayed()
    }

    @Test
    fun savedListShowsEmptyState() {
        composeRule.setContent {
            MaterialTheme {
                SavedProgramsContent(
                    items = emptyList(),
                    pendingIds = emptySet(),
                    onProgramClick = {},
                    onRemove = {},
                    onExplorePrograms = {}
                )
            }
        }

        composeRule.onNodeWithText("No saved programs yet").assertIsDisplayed()
        composeRule.onNodeWithText("Explore programs").assertIsDisplayed()
    }

    @Test
    fun saveButtonReflectsSavedAndUnsavedStates() {
        composeRule.setContent {
            MaterialTheme {
                androidx.compose.foundation.layout.Column {
                    ProgramSaveButton(false, false, {})
                    ProgramSaveButton(true, false, {})
                }
            }
        }

        composeRule.onNodeWithText("Save").assertIsDisplayed()
        composeRule.onNodeWithText("Saved").assertIsDisplayed()
    }
}
