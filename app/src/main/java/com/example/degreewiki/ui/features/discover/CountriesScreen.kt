package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.ui.components.BrowseResultsHeader
import com.example.degreewiki.ui.components.BrowseSearchBar
import com.example.degreewiki.ui.components.CountryBrowseCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SearchEmptyState

@Composable
fun CountriesScreen(
    onItemClick: (String) -> Unit,
    viewModel: CountriesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val discovery = state.discovery) {
        is DiscoveryUiState.Loading -> LoadingState(modifier, "Loading destinations")
        is DiscoveryUiState.Error -> ErrorState("Destinations unavailable", "We could not load study destinations right now. Check your connection and try again.", "Retry", viewModel::refresh, modifier)
        is DiscoveryUiState.Success -> CountriesContent(state, discovery, viewModel::setQuery, viewModel::clearSearch, onItemClick, viewModel::refresh, modifier)
    }
}

@Composable
internal fun CountriesContent(
    state: CountriesBrowseUiState,
    successState: DiscoveryUiState.Success<Country>,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onItemClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.totalCount == 0) {
        EmptyState("No destinations available", "We couldn't find any study destinations right now.", "Refresh", onRefresh, modifier)
        return
    }
    val hasQuery = normalizeQuery(state.query).isNotBlank()
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHero("Study destinations", "Search countries and destination summaries.") }
        item { BrowseSearchBar(state.query, onQueryChange, "Search study destinations") }
        item { BrowseResultsHeader(state.results.size, state.totalCount, hasQuery, "country") }
        if (successState.showRefreshWarning) item { RefreshWarningNote("Showing saved information. We could not refresh right now.", "Retry", onRefresh) }
        if (state.results.isEmpty()) {
            item { SearchEmptyState("No destinations found", "Try another country or keyword.", hasQuery, false, onClearSearch, {}) }
        } else {
            items(state.results, key = { it.id }) { CountryBrowseCard(it, onClick = { onItemClick(it.id) }) }
        }
    }
}
