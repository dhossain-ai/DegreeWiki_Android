package com.example.degreewiki.ui.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.degreewiki.data.repository.AuthState
import com.example.degreewiki.ui.features.auth.AuthViewModel
import com.example.degreewiki.ui.features.auth.LoginScreen
import com.example.degreewiki.ui.features.discover.CountriesScreen
import com.example.degreewiki.ui.features.discover.ProgramsScreen
import com.example.degreewiki.ui.features.discover.UniversitiesScreen
import com.example.degreewiki.ui.features.home.HomeScreen
import com.example.degreewiki.ui.features.profile.ProfileScreen

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var currentTab by rememberSaveable { mutableStateOf(DiscoveryTab.HOME) }
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

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
                onProgramsClick = { currentTab = DiscoveryTab.PROGRAMS },
                onUniversitiesClick = { currentTab = DiscoveryTab.UNIVERSITIES },
                onDestinationsClick = { currentTab = DiscoveryTab.COUNTRIES },
                modifier = Modifier.padding(innerPadding)
            )
            DiscoveryTab.PROGRAMS -> ProgramsScreen(
                onItemClick = { id -> onItemClick(com.example.degreewiki.ui.navigation.ProgramDetail(id)) },
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
                when (authState) {
                    is AuthState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is AuthState.Unauthenticated -> {
                        LoginScreen(modifier = Modifier.padding(innerPadding))
                    }
                    is AuthState.Authenticated -> {
                        ProfileScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
