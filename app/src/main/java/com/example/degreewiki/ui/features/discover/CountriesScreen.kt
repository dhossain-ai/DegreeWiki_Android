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
import com.example.degreewiki.ui.components.BrowseSectionHeader
import com.example.degreewiki.ui.components.CountryBrowseCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero

@Composable
fun CountriesScreen(
    onItemClick: (String) -> Unit,
    viewModel: CountriesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is DiscoveryUiState.Loading -> LoadingState(modifier = modifier, label = "Loading destinations")
        is DiscoveryUiState.Error -> ErrorState(
            title = "Destinations unavailable",
            message = "We could not load study destinations right now. Check your connection and try again.",
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
                    message = "We couldn't find any study destinations right now.",
                    actionLabel = "Refresh",
                    onActionClick = viewModel::refresh,
                    modifier = modifier
                )
            } else {
                DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        ScreenHero(
                            title = "Study destinations",
                            subtitle = "Explore countries and plan where to study abroad."
                        )
                    }
                    item {
                        BrowseSectionHeader(
                            title = "${data.size} countries",
                            subtitle = "Choose a destination to explore study costs, universities, and guidance."
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
                        CountryBrowseCard(country = country, onClick = { onItemClick(country.id) })
                    }
                }
            }
        }
    }
}
