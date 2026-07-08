package com.example.degreewiki.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.degreewiki.ui.features.main.MainScreen

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Main)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Main> {
                MainScreen(
                    onItemClick = { navKey -> backStack.add(navKey) },
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(16.dp)
                )
            }
            entry<ProgramDetail> {
                com.example.degreewiki.ui.features.details.ProgramDetailScreen(
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
            entry<UniversityDetail> {
                com.example.degreewiki.ui.features.details.UniversityDetailScreen(
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
            entry<CountryDetail> {
                com.example.degreewiki.ui.features.details.CountryDetailScreen(
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}

