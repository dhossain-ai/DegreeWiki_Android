package com.example.degreewiki.ui.features.guides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.GuideBrowseCard
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.features.discover.DiscoveryUiState

@Composable
fun GuidesScreen(onItemClick: (String) -> Unit, modifier: Modifier = Modifier, viewModel: GuidesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val current = state) {
        DiscoveryUiState.Loading -> LoadingState(modifier, "Loading study guides")
        DiscoveryUiState.Error -> ErrorState("Study guides unavailable", "We could not load study guides right now. Check your connection and try again.", "Retry", viewModel::refresh, modifier)
        is DiscoveryUiState.Success -> GuidesContent(current, onItemClick, viewModel::refresh, modifier)
    }
}

@Composable
internal fun GuidesContent(state: DiscoveryUiState.Success<Guide>, onItemClick: (String) -> Unit, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    if (state.data.isEmpty()) {
        EmptyState("No study guides are available right now", "Check again later for new practical guidance.", "Refresh", onRefresh, modifier)
        return
    }
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHero("Study guides", "Practical guidance for planning your studies abroad.") }
        if (state.showRefreshWarning) item { RefreshWarningNote("Showing saved guides. We could not refresh right now.", "Retry", onRefresh) }
        items(state.data, key = { it.id }) { guide -> GuideBrowseCard(guide, { onItemClick(guide.slug) }) }
    }
}
