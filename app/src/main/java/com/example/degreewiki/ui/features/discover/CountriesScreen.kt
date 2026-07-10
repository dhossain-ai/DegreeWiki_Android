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
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SectionHeader

@Composable
fun CountriesScreen(
    onItemClick: (String) -> Unit,
    viewModel: CountriesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(
            modifier = modifier,
            label = "Loading destinations"
        )
        is DiscoveryUiState.Error -> ErrorState(
            title = "Destinations unavailable",
            message = "We could not load destination data right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        is DiscoveryUiState.Success -> {
            val successState = state as DiscoveryUiState.Success<Country>
            val data = successState.data
            if (data.isEmpty()) {
                EmptyState(
                    title = "No destinations available",
                    message = "The app does not have any cached destination records yet.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier) {
                    item {
                        ScreenHero(
                            title = "Destinations",
                            subtitle = "Browse country and destination summaries already available in the app."
                        )
                    }
                    item {
                        SectionHeader(
                            title = "${data.size} destinations available",
                            subtitle = "Open any destination for the safe cached detail view."
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
                    items(data, key = { it.id }) { country ->
                        CountryCard(
                            country = country,
                            onClick = { onItemClick(country.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CountryCard(country: Country, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DegreeWikiCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = country.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = country.summary ?: "A destination summary is not available in the current mobile cache.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
