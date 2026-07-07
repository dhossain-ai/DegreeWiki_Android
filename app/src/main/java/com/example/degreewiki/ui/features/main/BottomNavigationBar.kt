package com.example.degreewiki.ui.features.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class DiscoveryTab {
    PROGRAMS, UNIVERSITIES, COUNTRIES
}

@Composable
fun BottomNavigationBar(
    currentTab: DiscoveryTab,
    onTabSelected: (DiscoveryTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Programs") },
            label = { Text("Programs") },
            selected = currentTab == DiscoveryTab.PROGRAMS,
            onClick = { onTabSelected(DiscoveryTab.PROGRAMS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Place, contentDescription = "Universities") },
            label = { Text("Universities") },
            selected = currentTab == DiscoveryTab.UNIVERSITIES,
            onClick = { onTabSelected(DiscoveryTab.UNIVERSITIES) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Countries") },
            label = { Text("Countries") },
            selected = currentTab == DiscoveryTab.COUNTRIES,
            onClick = { onTabSelected(DiscoveryTab.COUNTRIES) }
        )
    }
}
