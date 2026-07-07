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

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by rememberSaveable { mutableStateOf(DiscoveryTab.PROGRAMS) }

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
            DiscoveryTab.PROGRAMS -> ProgramsScreen(modifier = Modifier.padding(innerPadding))
            DiscoveryTab.UNIVERSITIES -> UniversitiesScreen(modifier = Modifier.padding(innerPadding))
            DiscoveryTab.COUNTRIES -> CountriesScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
