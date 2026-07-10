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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProgramDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.ProgramDetail,
    onBackClick: () -> Unit,
) {
    val viewModel = hiltViewModel<ProgramDetailViewModel, ProgramDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DetailTopAppBar(
                title = uiState.program?.title ?: "Program details",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(
                modifier = Modifier.padding(innerPadding),
                label = "Loading program details"
            )

            uiState.program == null -> DetailUnavailableState(
                title = "Program unavailable",
                message = "We could not load this program right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )

            else -> {
                val program = uiState.program!!
                val facts = buildList {
                    program.subject?.takeIf { it.isNotBlank() }?.let { add("Subject" to it) }
                    program.duration?.takeIf { it.isNotBlank() }?.let { add("Duration" to it) }
                    program.tuition?.let {
                        add(
                            "Tuition" to NumberFormat.getCurrencyInstance(Locale.US).format(it)
                        )
                    }
                }

                DegreeWikiScreen(modifier = Modifier.padding(innerPadding)) {
                    item {
                        DetailHeroCard(
                            title = program.title,
                            subtitle = program.universityName,
                            badge = program.degreeLevel,
                            supportingLines = listOf(program.countryName)
                        )
                    }
                    item {
                        DetailFactsCard(
                            title = "Quick facts",
                            subtitle = "Only fields already available in the current mobile record are shown.",
                            facts = facts
                        )
                    }
                    item {
                        DetailTrustNote(
                            text = "Details can change. Confirm final information on the official university page before applying."
                        )
                    }
                    item {
                        DetailFooterAction(
                            text = "Back to programs",
                            onClick = onBackClick
                        )
                    }
                }
            }
        }
    }
}
