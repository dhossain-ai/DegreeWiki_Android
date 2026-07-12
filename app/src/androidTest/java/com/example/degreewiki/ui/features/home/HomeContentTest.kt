package com.example.degreewiki.ui.features.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchEntry_routesToPrograms() {
        var programsOpened = false
        composeTestRule.setContent {
            DegreeWikiTheme {
                HomeContent(
                    state = HomeUiState(),
                    onProgramsClick = { programsOpened = true },
                    onUniversitiesClick = {},
                    onDestinationsClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.onNodeWithText("What do you want to study?").assertExists().performClick()
        composeTestRule.runOnIdle { assertTrue(programsOpened) }
    }
}
