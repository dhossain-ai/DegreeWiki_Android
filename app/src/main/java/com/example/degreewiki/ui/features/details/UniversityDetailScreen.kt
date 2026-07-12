package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState

@Composable
fun UniversityDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.UniversityDetail,
    onBackClick: () -> Unit,
    onProgramClick: (String) -> Unit
) {
    val viewModel = hiltViewModel<UniversityDetailViewModel, UniversityDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { DetailTopBar(title = "University", onBackClick = onBackClick) }) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding), "Loading university details")
            uiState.university == null -> DetailUnavailableState(
                title = "University unavailable",
                message = "We could not load this university right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )
            else -> UniversityDetailContent(
                uiState = uiState,
                onProgramClick = onProgramClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
internal fun UniversityDetailContent(
    uiState: UniversityDetailUiState,
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val university = uiState.university ?: return
    val detail = uiState.detail
    val location = listOfNotNull(
        detail?.city?.name?.takeIf(String::isNotBlank) ?: university.city?.takeIf(String::isNotBlank),
        detail?.country?.name?.takeIf(String::isNotBlank) ?: uiState.countryName?.takeIf(String::isNotBlank)
    ).distinct().joinToString(", ")
    val overview = detail?.overview?.takeIf(String::isNotBlank) ?: university.overview?.takeIf(String::isNotBlank)
    val facts = listOfNotNull(
        location.takeIf(String::isNotBlank)?.let { "Location" to it },
        detail?.institutionType?.takeIf(String::isNotBlank)?.let { "Institution type" to it },
        detail?.ownershipType?.takeIf(String::isNotBlank)?.let { "Ownership" to it },
        detail?.foundedYear?.let { "Founded" to it.toString() },
        detail?.studentCount?.let { "Students" to NumberFormatHelper.integer(it) },
        uiState.relatedPrograms.takeIf(List<*>::isNotEmpty)?.let { "Programs" to it.size.toString() }
    )

    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            DetailHero(
                title = university.name,
                location = location,
                showInitials = true
            )
        }
        detail?.officialUrl?.let { item { DetailActionRow(listOf("Visit university website" to it)) } }
        item { KeyFactsGrid(facts) }
        overview?.let { item { ExpandableTextSection("About", it) } }
        detail?.rankingSummary?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Rankings", it) } }
        detail?.admissionOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Admissions", it) } }
        detail?.applicationOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Applications", it) } }
        detail?.languageRequirementsSummary?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Language requirements", it) } }
        detail?.scholarshipsSummary?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Scholarships", it) } }
        detail?.housingSummary?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("Housing", it) } }
        detail?.internationalStudentSummary?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("International student support", it) } }
        detail?.studentLifeSummary?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("Student life", it) } }
        detail?.careerSupportSummary?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("Career support", it) } }
        if (uiState.relatedPrograms.isNotEmpty()) {
            item {
                DetailSection("Programs at this university") {
                    uiState.relatedPrograms.forEachIndexed { index, program ->
                        if (index > 0) HorizontalDivider()
                        RelatedContentRow(
                            title = program.title,
                            subtitle = listOfNotNull(program.degreeLevel, program.duration).filter(String::isNotBlank).joinToString(" · "),
                            onClick = { onProgramClick(program.id) }
                        )
                    }
                }
            }
        }
        item { SourceStatusSection(detail?.verificationStatus, detail?.lastVerifiedAt) }
    }
}

private object NumberFormatHelper {
    fun integer(value: Int): String = java.text.NumberFormat.getIntegerInstance().format(value)
}
