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
                    uiState.detail?.location?.takeIf { it.isNotBlank() }?.let { add("Location" to it) }
                    uiState.detail?.language?.takeIf { it.isNotBlank() }?.let { add("Language" to it) }
                    uiState.detail?.studyModeLabel?.takeIf { it.isNotBlank() }?.let { add("Study mode" to it) }
                    uiState.detail?.deliveryModeLabel?.takeIf { it.isNotBlank() }?.let { add("Delivery" to it) }
                    uiState.detail?.tuition?.display?.takeIf { it.isNotBlank() }?.let { add("Tuition" to it) }
                    uiState.detail?.nextIntake?.takeIf { it.isNotBlank() }?.let { add("Next intake" to it) }
                    uiState.detail?.nextDeadline?.takeIf { it.isNotBlank() }?.let { add("Next deadline" to it) }
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
                    listOf(
                        "Admission requirements" to uiState.detail?.admissionRequirements,
                        "English requirements" to uiState.detail?.englishRequirements,
                        "GPA requirements" to uiState.detail?.gpaRequirements,
                        "Curriculum" to uiState.detail?.curriculumSummary,
                        "Career outcomes" to uiState.detail?.careerOutcomes,
                        "Official program page" to uiState.detail?.officialUrl,
                        "Application page" to uiState.detail?.applicationUrl
                    ).forEach { (title, body) -> body?.takeIf { it.isNotBlank() }?.let { item { DetailTextSection(title, it) } } }
                    uiState.detail?.verificationStatus?.takeIf { it.isNotBlank() }?.let { status ->
                        item { DetailFactsCard("Source status", facts = listOfNotNull(
                            "Verification" to status.replace('_', ' '),
                            uiState.detail?.lastVerifiedAt?.let { "Last verified" to it }
                        )) }
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
