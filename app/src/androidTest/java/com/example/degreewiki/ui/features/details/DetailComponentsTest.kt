package com.example.degreewiki.ui.features.details

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.degreewiki.data.network.dto.UniversityDetailDto
import com.example.degreewiki.data.network.dto.CountryDetailDto
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailComponentsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun expandableText_collapsesAndExpands() {
        val longText = "A detailed paragraph about studying abroad. ".repeat(20)
        composeTestRule.setContent {
            DegreeWikiTheme { ExpandableTextSection(title = "About", body = longText) }
        }

        composeTestRule.onNodeWithText("Read more").performClick()
        composeTestRule.onNodeWithText("Show less").assertExists().performClick()
        composeTestRule.onNodeWithText("Read more").assertExists()
    }

    @Test
    fun externalLink_hidesRawUrl_andUnsafeOrMissingActions() {
        var openedUrl: String? = null
        composeTestRule.setContent {
            DegreeWikiTheme {
                Column {
                    ExternalLinkButton(
                        label = "Official program page",
                        url = "https://example.edu/program",
                        onOpen = { openedUrl = it }
                    )
                    ExternalLinkButton(label = "Unsafe link", url = "javascript:alert(1)")
                    ExternalLinkButton(label = "Missing link", url = null)
                }
            }
        }

        composeTestRule.onNodeWithText("Official program page").performClick()
        composeTestRule.onNodeWithText("https://example.edu/program").assertDoesNotExist()
        composeTestRule.onNodeWithText("Unsafe link").assertDoesNotExist()
        composeTestRule.onNodeWithText("Missing link").assertDoesNotExist()
        composeTestRule.runOnIdle { assertEquals("https://example.edu/program", openedUrl) }
    }

    @Test
    fun sourceStatus_doesNotTreatUnverifiedAsVerified() {
        composeTestRule.setContent {
            DegreeWikiTheme { SourceStatusSection(status = "unverified", lastChecked = null) }
        }

        composeTestRule.onNodeWithText("Not independently verified").assertExists()
        composeTestRule.onNodeWithText("Information checked against public sources.").assertDoesNotExist()
    }

    @Test
    fun faq_isCollapsedUntilQuestionIsTapped() {
        composeTestRule.setContent {
            DegreeWikiTheme {
                FaqAccordionItem(
                    question = "Can I work while studying?",
                    answer = "Check the official visa rules for your situation."
                )
            }
        }

        composeTestRule.onNodeWithText("Check the official visa rules for your situation.").assertDoesNotExist()
        composeTestRule.onNodeWithText("Can I work while studying?").performClick()
        composeTestRule.onNodeWithText("Check the official visa rules for your situation.").assertExists()
    }

    @Test
    fun universityOverview_rendersOnce_andRelatedProgramNavigates() {
        val overview = "A focused university overview shown in one place."
        var openedProgramId: String? = null
        composeTestRule.setContent {
            DegreeWikiTheme {
                UniversityDetailContent(
                    uiState = UniversityDetailUiState(
                        university = University("u1", "uni", "Example University", "c1", "Berlin", null, overview),
                        relatedPrograms = listOf(
                            Program("p1", "program", "Computer Science", "Example University", "Germany", "Master's", null, null, null)
                        ),
                        isLoading = false,
                        detail = UniversityDetailDto(overview = overview)
                    ),
                    onProgramClick = { openedProgramId = it }
                )
            }
        }

        composeTestRule.onAllNodesWithText(overview).assertCountEquals(1)
        composeTestRule.onNodeWithText("Computer Science").performClick()
        composeTestRule.runOnIdle { assertEquals("p1", openedProgramId) }
    }

    @Test
    fun programContent_omitsMissingSectionsAndBottomBackButton() {
        composeTestRule.setContent {
            DegreeWikiTheme {
                ProgramDetailContent(
                    ProgramDetailUiState(
                        program = Program("p1", "program", "Computer Science", "Example University", "Germany", "Master's", null, null, null),
                        isLoading = false,
                        detail = null
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Admission requirements").assertDoesNotExist()
        composeTestRule.onNodeWithText("Back to programs").assertDoesNotExist()
        composeTestRule.onNodeWithText("Not independently verified").assertExists()
    }

    @Test
    fun countryOverview_isNotRepeatedWhenSummaryMatchesOverview() {
        val overview = "One authoritative destination overview."
        composeTestRule.setContent {
            DegreeWikiTheme {
                CountryDetailContent(
                    uiState = CountryDetailUiState(
                        country = Country("c1", "country", "Exampleland", overview, null),
                        isLoading = false,
                        detail = CountryDetailDto(overview = overview, destinationSummary = overview)
                    ),
                    onProgramClick = {},
                    onUniversityClick = {}
                )
            }
        }

        composeTestRule.onAllNodesWithText(overview).assertCountEquals(1)
    }
}
