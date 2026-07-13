package com.example.degreewiki.ui.features.scholarships

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScholarshipBrowseCard
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.features.discover.DiscoveryUiState

@Composable
fun ScholarshipsScreen(onItemClick: (String) -> Unit, modifier: Modifier = Modifier, viewModel: ScholarshipsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val current = state) {
        DiscoveryUiState.Loading -> LoadingState(modifier, "Loading scholarships")
        DiscoveryUiState.Error -> ErrorState("Scholarships unavailable", "We could not load scholarships right now. Check your connection and try again.", "Retry", viewModel::refresh, modifier)
        is DiscoveryUiState.Success -> ScholarshipsContent(current, onItemClick, viewModel::refresh, modifier)
    }
}

@Composable
internal fun ScholarshipsContent(state: DiscoveryUiState.Success<Scholarship>, onItemClick: (String) -> Unit, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    if (state.data.isEmpty()) {
        EmptyState("No scholarships are available right now", "Check again later for new funding opportunities.", "Refresh", onRefresh, modifier)
        return
    }
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHero("Scholarships", "Explore funding opportunities for international study.") }
        if (state.showRefreshWarning) item { RefreshWarningNote("Showing saved scholarships. We could not refresh right now.", "Retry", onRefresh) }
        items(state.data, key = { it.id }) { scholarship -> ScholarshipBrowseCard(scholarship, { onItemClick(scholarship.slug) }) }
    }
}
