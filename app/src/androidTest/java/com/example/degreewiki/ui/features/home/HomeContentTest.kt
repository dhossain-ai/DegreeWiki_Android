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
                    onScholarshipsClick = {},
                    onGuidesClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Search programs, subjects, or universities").performTextInput("computer science")
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.runOnIdle { assertEquals("computer science", submittedQuery) }
    }

    @Test
    fun scholarshipAndGuideEntries_areActive() {
        var destination = ""
        composeTestRule.setContent {
            DegreeWikiTheme {
                HomeContent(
                    state = HomeUiState(),
                    onProgramsClick = {},
                    onUniversitiesClick = {},
                    onDestinationsClick = {},
                    onScholarshipsClick = { destination = "scholarships" },
                    onGuidesClick = { destination = "guides" },
                    onRefresh = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Scholarships").performClick()
        composeTestRule.runOnIdle { assertEquals("scholarships", destination) }
        composeTestRule.onNodeWithText("Guides").performClick()
        composeTestRule.runOnIdle { assertEquals("guides", destination) }
    }
}
