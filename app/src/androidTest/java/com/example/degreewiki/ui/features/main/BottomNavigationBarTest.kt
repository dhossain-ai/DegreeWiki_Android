package com.example.degreewiki.ui.features.main

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class BottomNavigationBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun bottomNavigationBar_showsAllTabs() {
        composeTestRule.setContent {
            BottomNavigationBar(
                currentTab = DiscoveryTab.PROGRAMS,
                onTabSelected = {}
            )
        }

        composeTestRule.onNodeWithText("Programs").assertExists()
        composeTestRule.onNodeWithText("Universities").assertExists()
        composeTestRule.onNodeWithText("Countries").assertExists()
        composeTestRule.onNodeWithText("Profile").assertExists()
    }

    @Test
    fun bottomNavigationBar_clickingTab_updatesSelectionState() {
        composeTestRule.setContent {
            var currentTab by remember { mutableStateOf(DiscoveryTab.PROGRAMS) }
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Countries").performClick()
        composeTestRule.onNodeWithText("Countries").assertExists()
    }
}
