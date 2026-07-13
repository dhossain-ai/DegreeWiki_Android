package com.example.degreewiki.ui.features.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchEntry_routesToPrograms() {
        var submittedQuery: String? = null
        composeTestRule.setContent {
            DegreeWikiTheme {
                HomeContent(
                    state = HomeUiState(),
                    onProgramsClick = { submittedQuery = it },
                    onUniversitiesClick = {},
                    onDestinationsClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Search programs, subjects, or universities").performTextInput("computer science")
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.runOnIdle { assertEquals("computer science", submittedQuery) }
    }
}
