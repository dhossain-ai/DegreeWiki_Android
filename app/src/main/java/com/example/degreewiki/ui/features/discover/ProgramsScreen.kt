package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.degreewiki.domain.model.Program
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
fun ProgramsScreen(
    onItemClick: (String) -> Unit,
    viewModel: ProgramsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(
            modifier = modifier,
            label = "Loading programs"
        )
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
                    message = "The app does not have any cached program records yet.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier) {
                    item {
                        ScreenHero(
                            title = "Programs",
                            subtitle = "Browse real degree records from the current mobile catalog."
                        )
                    }
                    item {
                        SectionHeader(
                            title = "${data.size} programs available",
                            subtitle = "Open any card to view the cached detail screen."
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
                        ProgramCard(
                            program = program,
                            onClick = { onItemClick(program.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramCard(program: Program, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DegreeWikiCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = program.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = program.universityName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = program.countryName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusBadge(
                    label = program.degreeLevel,
                    tone = StatusBadgeTone.Neutral
                )
                program.subject?.let {
                    StatusBadge(
                        label = it,
                        tone = StatusBadgeTone.Neutral
                    )
                }
            }
            program.duration?.let {
                Text(
                    text = "Duration: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
