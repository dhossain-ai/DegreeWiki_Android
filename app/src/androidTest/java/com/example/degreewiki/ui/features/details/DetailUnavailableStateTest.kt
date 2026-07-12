package com.example.degreewiki.ui.features.details

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class DetailUnavailableStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailUnavailableState_showsFriendlyCopyAndAction() {
        composeTestRule.setContent {
            DetailUnavailableState(
                title = "Program unavailable",
                message = "We could not load this program right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = {}
            )
        }

        composeTestRule.onNodeWithText("Program unavailable").assertExists()
        composeTestRule.onNodeWithText(
            "We could not load this program right now. Go back and try opening it again."
        ).assertExists()
        composeTestRule.onNodeWithText("Go back").assertExists()
    }
}
