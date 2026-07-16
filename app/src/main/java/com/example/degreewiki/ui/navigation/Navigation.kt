package com.example.degreewiki.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.degreewiki.ui.features.main.MainScreen

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Main())

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Main> { key ->
                MainScreen(
                    onItemClick = { navKey -> backStack.add(navKey) },
                    initialTab = runCatching {
                        com.example.degreewiki.ui.features.main.DiscoveryTab.valueOf(key.initialTab)
                    }.getOrDefault(com.example.degreewiki.ui.features.main.DiscoveryTab.HOME),
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(16.dp)
                )
            }
            entry<Login> {
                com.example.degreewiki.ui.features.auth.LoginScreen(
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
            entry<SavedPrograms> {
                com.example.degreewiki.ui.features.profile.SavedProgramsScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    onProgramClick = { id -> backStack.add(ProgramDetail(id)) },
                    onExplorePrograms = {
                        backStack.clear()
                        backStack.add(Main(initialTab = "PROGRAMS"))
                    },
                    onLoginRequired = {
                        backStack.removeLastOrNull()
                        backStack.add(Login)
                    }
                )
            }
            entry<ProgramDetail> { key ->
                com.example.degreewiki.ui.features.details.ProgramDetailScreen(
                    navKey = key,
                    onBackClick = { backStack.removeLastOrNull() },
                    onLoginRequired = { backStack.add(Login) }
                )
            }
            entry<UniversityDetail> { key ->
                com.example.degreewiki.ui.features.details.UniversityDetailScreen(
                    navKey = key,
                    onBackClick = { backStack.removeLastOrNull() },
                    onProgramClick = { id -> backStack.add(ProgramDetail(id)) }
                )
            }
            entry<CountryDetail> { key ->
                com.example.degreewiki.ui.features.details.CountryDetailScreen(
                    navKey = key,
                    onBackClick = { backStack.removeLastOrNull() },
                    onProgramClick = { id -> backStack.add(ProgramDetail(id)) },
                    onUniversityClick = { id -> backStack.add(UniversityDetail(id)) }
                )
            }
            entry<Scholarships> {
                com.example.degreewiki.ui.features.scholarships.ScholarshipsScreen(
                    onItemClick = { slug -> backStack.add(ScholarshipDetail(slug)) },
                    modifier = Modifier.safeDrawingPadding().padding(16.dp)
                )
            }
            entry<ScholarshipDetail> { key ->
                com.example.degreewiki.ui.features.scholarships.ScholarshipDetailScreen(
                    navKey = key,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
            entry<Guides> {
                com.example.degreewiki.ui.features.guides.GuidesScreen(
                    onItemClick = { slug -> backStack.add(GuideDetail(slug)) },
                    modifier = Modifier.safeDrawingPadding().padding(16.dp)
                )
            }
            entry<GuideDetail> { key ->
                com.example.degreewiki.ui.features.guides.GuideDetailScreen(
                    navKey = key,
                    onBackClick = { backStack.removeLastOrNull() },
                    onRelatedGuideClick = { slug -> backStack.add(GuideDetail(slug)) }
                )
            }
        }
    )
}
