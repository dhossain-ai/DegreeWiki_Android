package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.ui.components.ActiveFilterChips
import com.example.degreewiki.ui.components.BrowseFilterButton
import com.example.degreewiki.ui.components.BrowseResultsHeader
import com.example.degreewiki.ui.components.BrowseSearchBar
import com.example.degreewiki.ui.components.BrowseSortButton
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.ProgramBrowseCard
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SearchEmptyState
import com.example.degreewiki.ui.components.LoginToSavePrompt
import com.example.degreewiki.data.repository.SaveProgramResult

@Composable
fun ProgramsScreen(
    onItemClick: (String) -> Unit,
    queryRequest: Pair<Long, String>? = null,
    onQueryRequestConsumed: () -> Unit = {},
    onLoginRequired: () -> Unit = {},
    viewModel: ProgramsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var showSort by rememberSaveable { mutableStateOf(false) }
    var showLoginPrompt by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.saveEvents.collect { result ->
            when (result) {
                SaveProgramResult.LoginRequired -> showLoginPrompt = true
                is SaveProgramResult.Failure -> snackbarHostState.showSnackbar(result.message)
                SaveProgramResult.Success -> Unit
            }
        }
    }

    LaunchedEffect(queryRequest?.first) {
        queryRequest?.let {
            viewModel.setQuery(it.second)
            onQueryRequestConsumed()
        }
    }

    when (val discovery = state.discovery) {
        is DiscoveryUiState.Loading -> LoadingState(modifier = modifier, label = "Loading programs")
        is DiscoveryUiState.Error -> ErrorState(
            title = "Programs unavailable",
            message = "We could not load programs right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        is DiscoveryUiState.Success -> ProgramsContent(
            state = state,
            successState = discovery,
            onQueryChange = viewModel::setQuery,
            onFilterClick = { showFilters = true },
            onSortClick = { showSort = true },
            onRemoveFilter = viewModel::removeFilter,
            onClearFilters = viewModel::clearFilters,
            onClearSearch = viewModel::clearSearch,
            onItemClick = onItemClick,
            onSaveClick = viewModel::toggleSaved,
            onRefresh = viewModel::refresh,
            modifier = modifier
        )
    }

    Box(modifier = modifier.fillMaxWidth()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showLoginPrompt) {
        LoginToSavePrompt(
            onDismiss = { showLoginPrompt = false },
            onLogIn = {
                showLoginPrompt = false
                onLoginRequired()
            }
        )
    }

    if (showFilters) {
        ProgramFilterBottomSheet(
            current = state.search.filters,
            options = state.filterOptions,
            onApply = { viewModel.setFilters(it); showFilters = false },
            onDismiss = { showFilters = false }
        )
    }
    if (showSort) {
        ProgramSortBottomSheet(
            current = state.search.sort,
            onSelect = { viewModel.setSort(it); showSort = false },
            onDismiss = { showSort = false }
        )
    }
}

@Composable
internal fun ProgramsContent(
    state: ProgramsBrowseUiState,
    successState: DiscoveryUiState.Success<Program>,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onRemoveFilter: (String) -> Unit,
    onClearFilters: () -> Unit,
    onClearSearch: () -> Unit,
    onItemClick: (String) -> Unit,
    onSaveClick: (Program) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.totalCount == 0) {
        EmptyState(
            title = "No programs available",
            message = "We couldn't find any programs right now.",
            actionLabel = "Refresh",
            onActionClick = onRefresh,
            modifier = modifier
        )
        return
    }
    val filters = state.search.filters
    val activeFilters = filters.degreeLevels.toList() + filters.countries + filters.subjects + filters.universities
    val hasQuery = normalizeQuery(state.search.query).isNotBlank()
    val hasDiscovery = hasQuery || !filters.isEmpty

    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHero(title = "Programs", subtitle = "Search and compare degree programs from universities around the world.") }
        item {
            BrowseSearchBar(
                query = state.search.query,
                onQueryChange = onQueryChange,
                placeholder = "Search programs, subjects, or universities"
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrowseFilterButton(filters.count, onFilterClick, Modifier.weight(1f))
                BrowseSortButton(state.search.sort.label, onSortClick, Modifier.weight(1f))
            }
        }
        if (activeFilters.isNotEmpty()) {
            item { ActiveFilterChips(activeFilters, onRemoveFilter, onClearFilters) }
        }
        item { BrowseResultsHeader(state.results.size, state.totalCount, hasDiscovery, "program") }
        if (successState.showRefreshWarning) {
            item { RefreshWarningNote("Showing saved information. We could not refresh right now.", "Retry", onRefresh) }
        }
        if (state.results.isEmpty()) {
            item {
                SearchEmptyState(
                    title = "No programs found",
                    message = "Try another keyword or remove some filters.",
                    hasQuery = hasQuery,
                    hasFilters = !filters.isEmpty,
                    onClearSearch = onClearSearch,
                    onClearFilters = onClearFilters
                )
            }
        } else {
            items(state.results, key = { it.id }) { program ->
                ProgramBrowseCard(
                    program = program,
                    onClick = { onItemClick(program.id) },
                    isSaved = program.id in state.savedProgramIds,
                    isSaveLoading = program.id in state.pendingProgramIds,
                    onSaveClick = { onSaveClick(program) }
                )
            }
        }
    }
}
