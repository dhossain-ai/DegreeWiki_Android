package com.example.degreewiki.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BrowseComponentsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun programCard_omitsMissingOptionalFacts_andRemainsClickable() {
        var clicked = false
        composeTestRule.setContent {
            DegreeWikiTheme {
                ProgramBrowseCard(
                    program = Program(
                        id = "program-1",
                        slug = "program-1",
                        title = "Computer Science",
                        universityName = "Example University",
                        countryName = "Germany",
                        degreeLevel = "Master's",
                        subject = null,
                        tuition = null,
                        duration = null
                    ),
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Computer Science").assertExists().performClick()
        composeTestRule.onNodeWithText("Tuition", substring = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("not available", substring = true, ignoreCase = true).assertDoesNotExist()
        composeTestRule.runOnIdle { assertTrue(clicked) }
    }

    @Test
    fun universityCard_omitsTechnicalFallback_whenOverviewIsMissing() {
        composeTestRule.setContent {
            DegreeWikiTheme {
                UniversityBrowseCard(
                    university = University(
                        id = "university-1",
                        slug = "university-1",
                        name = "Example University",
                        countryId = "country-1",
                        city = null,
                        logoUrl = null,
                        overview = null
                    ),
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Example University").assertExists()
        composeTestRule.onNodeWithText("cache", substring = true, ignoreCase = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("not available", substring = true, ignoreCase = true).assertDoesNotExist()
    }
}
