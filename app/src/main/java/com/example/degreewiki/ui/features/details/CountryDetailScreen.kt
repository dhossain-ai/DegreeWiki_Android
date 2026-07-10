package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState

@Composable
fun CountryDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.CountryDetail,
    onBackClick: () -> Unit,
) {
    val viewModel = hiltViewModel<CountryDetailViewModel, CountryDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DetailTopAppBar(
                title = uiState.country?.name ?: "Country details",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(
                modifier = Modifier.padding(innerPadding),
                label = "Loading country details"
            )

            uiState.country == null -> DetailUnavailableState(
                title = "Country unavailable",
                message = "We could not load this destination right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )

            else -> {
                val country = uiState.country!!

                DegreeWikiScreen(modifier = Modifier.padding(innerPadding)) {
                    item {
                        DetailHeroCard(
                            title = country.name,
                            badge = "Destination",
                            supportingLines = listOfNotNull(
                                country.summary?.takeIf { it.isNotBlank() }
                            )
                        )
                    }
                    country.summary?.takeIf { it.isNotBlank() }?.let { summary ->
                        item {
                            DetailTextSection(
                                title = "Destination summary",
                                body = summary
                            )
                        }
                    }
                    if (uiState.relatedUniversities.isNotEmpty()) {
                        item {
                            RelatedTextListCard(
                                title = "Universities in this destination",
                                subtitle = "Shown only when current cached university records match this country.",
                                items = uiState.relatedUniversities
                            )
                        }
                    }
                    if (uiState.relatedPrograms.isNotEmpty()) {
                        item {
                            RelatedTextListCard(
                                title = "Programs connected to this destination",
                                subtitle = "These are the cached program entries already tied to this country.",
                                items = uiState.relatedPrograms
                            )
                        }
                    }
                    item {
                        DetailTrustNote(
                            text = "Study, visa, and cost information can change. Always confirm details on official sources."
                        )
                    }
                    item {
                        DetailFooterAction(
                            text = "Back to countries",
                            onClick = onBackClick
                        )
                    }
                }
            }
        }
    }
}
