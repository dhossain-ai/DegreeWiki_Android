package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SectionHeader
import com.example.degreewiki.ui.components.StatusBadge
import com.example.degreewiki.ui.components.StatusBadgeTone

@Composable
fun UniversitiesScreen(
    onItemClick: (String) -> Unit,
    viewModel: UniversitiesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(
            modifier = modifier,
            label = "Loading universities"
        )
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
                    message = "The app does not have any cached university records yet.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier) {
                    item {
                        ScreenHero(
                            title = "Universities",
                            subtitle = "Browse institution profiles backed by the current mobile data feed."
                        )
                    }
                    item {
                        SectionHeader(
                            title = "${data.size} universities available",
                            subtitle = "Cards show only fields already available in the cache."
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
                        UniversityCard(
                            university = university,
                            onClick = { onItemClick(university.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UniversityCard(university: University, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DegreeWikiCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = university.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            university.city?.let {
                StatusBadge(
                    label = it,
                    tone = StatusBadgeTone.Neutral
                )
            }
            university.overview?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: Text(
                text = "Overview is not available in the current mobile cache.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
