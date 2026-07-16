package com.example.degreewiki.ui.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.example.degreewiki.ui.features.discover.CountriesScreen
import com.example.degreewiki.ui.features.discover.ProgramsScreen
import com.example.degreewiki.ui.features.discover.UniversitiesScreen
import com.example.degreewiki.ui.features.home.HomeScreen
import com.example.degreewiki.ui.features.profile.ProfileScreen

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    initialTab: DiscoveryTab = DiscoveryTab.HOME,
    modifier: Modifier = Modifier
) {
    var currentTab by rememberSaveable { mutableStateOf(initialTab) }
    var nextProgramQueryRequestId by rememberSaveable { mutableStateOf(0L) }
    var programQueryRequest by rememberSaveable { mutableStateOf<Pair<Long, String>?>(null) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (currentTab) {
            DiscoveryTab.HOME -> HomeScreen(
                onProgramsClick = { query ->
                    nextProgramQueryRequestId += 1
                    programQueryRequest = nextProgramQueryRequestId to query
                    currentTab = DiscoveryTab.PROGRAMS
                },
                onUniversitiesClick = { currentTab = DiscoveryTab.UNIVERSITIES },
                onDestinationsClick = { currentTab = DiscoveryTab.COUNTRIES },
                onScholarshipsClick = { onItemClick(com.example.degreewiki.ui.navigation.Scholarships) },
                onGuidesClick = { onItemClick(com.example.degreewiki.ui.navigation.Guides) },
                onLoginRequired = { onItemClick(com.example.degreewiki.ui.navigation.Login) },
                modifier = Modifier.padding(innerPadding)
            )
            DiscoveryTab.PROGRAMS -> ProgramsScreen(
                onItemClick = { id -> onItemClick(com.example.degreewiki.ui.navigation.ProgramDetail(id)) },
                onLoginRequired = { onItemClick(com.example.degreewiki.ui.navigation.Login) },
                queryRequest = programQueryRequest,
                onQueryRequestConsumed = { programQueryRequest = null },
                modifier = Modifier.padding(innerPadding)
            )
            DiscoveryTab.UNIVERSITIES -> UniversitiesScreen(
                onItemClick = { id -> onItemClick(com.example.degreewiki.ui.navigation.UniversityDetail(id)) },
                modifier = Modifier.padding(innerPadding)
            )
            DiscoveryTab.COUNTRIES -> CountriesScreen(
                onItemClick = { id -> onItemClick(com.example.degreewiki.ui.navigation.CountryDetail(id)) },
                modifier = Modifier.padding(innerPadding)
            )
            DiscoveryTab.PROFILE -> {
                ProfileScreen(
                    onLoginClick = { onItemClick(com.example.degreewiki.ui.navigation.Login) },
                    onSavedProgramsClick = {
                        onItemClick(com.example.degreewiki.ui.navigation.SavedPrograms)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
