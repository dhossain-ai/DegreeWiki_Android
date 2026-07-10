package com.example.degreewiki.ui.features.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

enum class DiscoveryTab {
    HOME, PROGRAMS, UNIVERSITIES, COUNTRIES, PROFILE
}

@Composable
fun BottomNavigationBar(
    currentTab: DiscoveryTab,
    onTabSelected: (DiscoveryTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            selected = currentTab == DiscoveryTab.HOME,
            onClick = { onTabSelected(DiscoveryTab.HOME) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Programs") },
            label = { Text("Programs", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            selected = currentTab == DiscoveryTab.PROGRAMS,
            onClick = { onTabSelected(DiscoveryTab.PROGRAMS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Place, contentDescription = "Universities") },
            label = { Text("Universities", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            selected = currentTab == DiscoveryTab.UNIVERSITIES,
            onClick = { onTabSelected(DiscoveryTab.UNIVERSITIES) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Countries") },
            label = { Text("Countries", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            selected = currentTab == DiscoveryTab.COUNTRIES,
            onClick = { onTabSelected(DiscoveryTab.COUNTRIES) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            selected = currentTab == DiscoveryTab.PROFILE,
            onClick = { onTabSelected(DiscoveryTab.PROFILE) }
        )
    }
}
