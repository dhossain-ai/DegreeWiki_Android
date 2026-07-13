package com.example.degreewiki.ui.features.guides

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.GuideBlock
import com.example.degreewiki.domain.model.GuideDetail
import com.example.degreewiki.domain.model.GuideInline
import com.example.degreewiki.ui.theme.DegreeWikiTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GuideDetailContentTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun rendersStructuredContentAndNavigatesToRelatedGuide() {
        val summary = Guide("g", "guide", "Main guide", "Summary", null, emptyList(), emptyList(), emptyList(), null, null, null)
        val related = Guide("r", "related", "Related guide", null, null, emptyList(), emptyList(), emptyList(), null, null, null)
        val detail = GuideDetail(
            summary, 4,
            listOf(
                GuideBlock.Heading(2, listOf(GuideInline.Plain("Heading"))),
                GuideBlock.Paragraph(listOf(GuideInline.Strong("Important paragraph"))),
                GuideBlock.UnorderedList(listOf(listOf(GuideInline.Plain("Bullet item"))))
            ),
            "verified", null, null, listOf(related)
        )
        var opened = ""
        composeTestRule.setContent { DegreeWikiTheme { GuideDetailContent(summary, detail, false, { opened = it }) } }
        composeTestRule.onNodeWithText("Heading").assertExists()
        composeTestRule.onNodeWithText("Important paragraph").assertExists()
        composeTestRule.onNodeWithText("Bullet item").assertExists()
        composeTestRule.onNodeWithText("Related guide").performClick()
        composeTestRule.runOnIdle { assertEquals("related", opened) }
    }
}
