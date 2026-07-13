package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.components.BrowseResultsHeader
import com.example.degreewiki.ui.components.BrowseSearchBar
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SearchEmptyState
import com.example.degreewiki.ui.components.UniversityBrowseCard

@Composable
fun UniversitiesScreen(
    onItemClick: (String) -> Unit,
    viewModel: UniversitiesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val discovery = state.discovery) {
        is DiscoveryUiState.Loading -> LoadingState(modifier, "Loading universities")
        is DiscoveryUiState.Error -> ErrorState("Universities unavailable", "We could not load universities right now. Check your connection and try again.", "Retry", viewModel::refresh, modifier)
        is DiscoveryUiState.Success -> UniversitiesContent(state, discovery, viewModel::setQuery, viewModel::clearSearch, onItemClick, viewModel::refresh, modifier)
    }
}

@Composable
internal fun UniversitiesContent(
    state: UniversitiesBrowseUiState,
    successState: DiscoveryUiState.Success<University>,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onItemClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.totalCount == 0) {
        EmptyState("No universities available", "We couldn't find any universities right now.", "Refresh", onRefresh, modifier)
        return
    }
    val hasQuery = normalizeQuery(state.query).isNotBlank()
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHero("Universities", "Search universities by name, city, or profile information.") }
        item { BrowseSearchBar(state.query, onQueryChange, "Search universities or cities") }
        item { BrowseResultsHeader(state.results.size, state.totalCount, hasQuery, "university") }
        if (successState.showRefreshWarning) item { RefreshWarningNote("Showing saved information. We could not refresh right now.", "Retry", onRefresh) }
        if (state.results.isEmpty()) {
            item { SearchEmptyState("No universities found", "Try another university name or city.", hasQuery, false, onClearSearch, {}) }
        } else {
            items(state.results, key = { it.id }) { UniversityBrowseCard(it, onClick = { onItemClick(it.id) }) }
        }
    }
}
