package com.example.degreewiki.ui.features.details

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState

@Composable
fun UniversityDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.UniversityDetail,
    onBackClick: () -> Unit,
) {
    val viewModel = hiltViewModel<UniversityDetailViewModel, UniversityDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DetailTopAppBar(
                title = uiState.university?.name ?: "University details",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(
                modifier = Modifier.padding(innerPadding),
                label = "Loading university details"
            )

            uiState.university == null -> DetailUnavailableState(
                title = "University unavailable",
                message = "We could not load this university right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )

            else -> {
                val university = uiState.university!!
                val location = listOfNotNull(
                    university.city?.takeIf { it.isNotBlank() },
                    uiState.countryName?.takeIf { it.isNotBlank() }
                ).joinToString(", ")
                val facts = buildList {
                    location.takeIf { it.isNotBlank() }?.let { add("Location" to it) }
                }

                DegreeWikiScreen(modifier = Modifier.padding(innerPadding)) {
                    item {
                        DetailHeroCard(
                            title = university.name,
                            subtitle = location.takeIf { it.isNotBlank() },
                            supportingLines = listOfNotNull(
                                university.overview?.takeIf { it.isNotBlank() }
                            )
                        )
                    }
                    item {
                        DetailFactsCard(
                            title = "Campus snapshot",
                            subtitle = "We omit any missing fields instead of showing placeholders.",
                            facts = facts
                        )
                    }
                    university.overview?.takeIf { it.isNotBlank() }?.let { overview ->
                        item {
                            DetailTextSection(
                                title = "Overview",
                                body = overview
                            )
                        }
                    }
                    if (uiState.relatedPrograms.isNotEmpty()) {
                        item {
                            RelatedTextListCard(
                                title = "Related programs",
                                subtitle = "These cached programs currently point to this university.",
                                items = uiState.relatedPrograms
                            )
                        }
                    }
                    item {
                        DetailTrustNote(
                            text = "University information may change. Check the official university website before applying."
                        )
                    }
                    item {
                        DetailFooterAction(
                            text = "Back to universities",
                            onClick = onBackClick
                        )
                    }
                }
            }
        }
    }
}
