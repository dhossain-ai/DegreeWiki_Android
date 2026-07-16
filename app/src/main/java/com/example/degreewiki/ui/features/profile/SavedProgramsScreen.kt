package com.example.degreewiki.ui.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.data.repository.ProfileState
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.domain.model.SavedProgram
import com.example.degreewiki.ui.components.CompactFactRow
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.MetadataBadge
import com.example.degreewiki.ui.components.SavedProgramsEmptyState
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.features.details.DetailTopBar
import kotlinx.coroutines.launch

@Composable
fun SavedProgramsScreen(
    onBackClick: () -> Unit,
    onProgramClick: (String) -> Unit,
    onExplorePrograms: () -> Unit,
    onLoginRequired: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val savedState by viewModel.savedProgramsState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { DetailTopBar("Saved programs", onBackClick) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        when {
            profileState is ProfileState.LoggedOut || profileState is ProfileState.SessionExpired ->
                LaunchedEffect(Unit) { onLoginRequired() }
            savedState.isRefreshing && savedState.items.isEmpty() -> LoadingState(
                modifier = Modifier.padding(padding),
                label = "Loading saved programs"
            )
            savedState.refreshFailed && savedState.items.isEmpty() -> ErrorState(
                title = "We couldn’t load your saved programs.",
                message = "Check your connection and try again.",
                actionLabel = "Retry",
                onActionClick = viewModel::refresh,
                modifier = Modifier.padding(padding)
            )
            else -> SavedProgramsContent(
                items = savedState.items,
                pendingIds = savedState.pendingProgramIds,
                onProgramClick = onProgramClick,
                onRemove = { programId ->
                    viewModel.removeSavedProgram(programId) { result ->
                        if (result is SaveProgramResult.Failure) {
                            scope.launch { snackbar.showSnackbar(result.message) }
                        }
                    }
                },
                onExplorePrograms = onExplorePrograms,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
internal fun SavedProgramsContent(
    items: List<SavedProgram>,
    pendingIds: Set<String>,
    onProgramClick: (String) -> Unit,
    onRemove: (String) -> Unit,
    onExplorePrograms: () -> Unit,
    modifier: Modifier = Modifier
) {
    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHero(
                title = "Saved programs",
                subtitle = "Programs you have added to your shortlist."
            )
        }
        if (items.isEmpty()) {
            item { SavedProgramsEmptyState(onExplorePrograms) }
        } else {
            items(items.size, key = { items[it].savedItemId }) { index ->
                SavedProgramCard(
                    item = items[index],
                    isRemoving = items[index].programId in pendingIds,
                    onClick = { onProgramClick(items[index].programId) },
                    onRemove = { onRemove(items[index].programId) }
                )
            }
        }
    }
}

@Composable
private fun SavedProgramCard(
    item: SavedProgram,
    isRemoving: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    DegreeWikiCard(onClick = onClick) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    item.universityName?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                    item.countryName?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onRemove, enabled = !isRemoving) {
                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Remove saved program")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.degreeLevel?.let { MetadataBadge(it) }
                item.subject?.let { MetadataBadge(it) }
            }
            CompactFactRow(
                listOfNotNull(
                    item.tuitionDisplay?.let { "Tuition $it" },
                    item.duration
                )
            )
        }
    }
}
