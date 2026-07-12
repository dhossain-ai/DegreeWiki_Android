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
import com.example.degreewiki.ui.components.BrowseSectionHeader
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.UniversityBrowseCard

@Composable
fun UniversitiesScreen(
    onItemClick: (String) -> Unit,
    viewModel: UniversitiesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(modifier = modifier, label = "Loading universities")
        is DiscoveryUiState.Error -> ErrorState(
            title = "Universities unavailable",
            message = "We could not load universities right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        is DiscoveryUiState.Success -> {
            val successState = state as DiscoveryUiState.Success<University>
            val data = successState.data
            if (data.isEmpty()) {
                EmptyState(
                    title = "No universities available",
                    message = "We couldn't find any universities right now.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        ScreenHero(
                            title = "Universities",
                            subtitle = "Explore universities, locations, programs, and admissions information."
                        )
                    }
                    item {
                        BrowseSectionHeader(
                            title = "${data.size} universities",
                            subtitle = "Choose a university to explore its profile and programs."
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
                    items(data, key = { it.id }) { university ->
                        UniversityBrowseCard(university = university, onClick = { onItemClick(university.id) })
                    }
                }
            }
        }
    }
}
