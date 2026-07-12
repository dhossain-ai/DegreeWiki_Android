package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.ui.components.BrowseSectionHeader
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.ProgramBrowseCard
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero

@Composable
fun ProgramsScreen(
    onItemClick: (String) -> Unit,
    viewModel: ProgramsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(modifier = modifier, label = "Loading programs")
        is DiscoveryUiState.Error -> ErrorState(
            title = "Programs unavailable",
            message = "We could not load programs right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        is DiscoveryUiState.Success -> {
            val successState = state as DiscoveryUiState.Success<Program>
            val data = successState.data
            if (data.isEmpty()) {
                EmptyState(
                    title = "No programs available",
                    message = "We couldn't find any programs right now.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier, verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                    item {
                        ScreenHero(
                            title = "Programs",
                            subtitle = "Explore degree programs from universities around the world."
                        )
                    }
                    item {
                        BrowseSectionHeader(
                            title = "${data.size} programs",
                            subtitle = "Choose a program to view tuition, duration, requirements, and more."
                        )
                    }
                    if (successState.showRefreshWarning) {
                        item {
                            RefreshWarningNote(
                                text = "Showing saved information. We could not refresh right now.",
                                actionLabel = "Retry",
                                onActionClick = viewModel::refresh
                            )
                        }
                    }
                    items(data, key = { it.id }) { program ->
                        ProgramBrowseCard(program = program, onClick = { onItemClick(program.id) })
                    }
                }
            }
        }
    }
}
