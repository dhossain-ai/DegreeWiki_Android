package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.ui.components.LoginToSavePrompt
import com.example.degreewiki.ui.components.ProgramSaveButton

@Composable
fun ProgramDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.ProgramDetail,
    onBackClick: () -> Unit,
    onLoginRequired: () -> Unit,
) {
    val viewModel = hiltViewModel<ProgramDetailViewModel, ProgramDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var showLoginPrompt by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.saveEvents.collect { result ->
            when (result) {
                SaveProgramResult.LoginRequired -> showLoginPrompt = true
                is SaveProgramResult.Failure -> snackbar.showSnackbar(result.message)
                SaveProgramResult.Success -> Unit
            }
        }
    }

    Scaffold(
        topBar = { DetailTopBar(title = "Program details", onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding), "Loading program details")
            uiState.program == null -> DetailUnavailableState(
                title = "Program unavailable",
                message = "We could not load this program right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )
            else -> ProgramDetailContent(
                uiState = uiState,
                onSaveClick = viewModel::toggleSaved,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (showLoginPrompt) {
        LoginToSavePrompt(
            onDismiss = { showLoginPrompt = false },
            onLogIn = {
                showLoginPrompt = false
                onLoginRequired()
            }
        )
    }
}

@Composable
internal fun ProgramDetailContent(
    uiState: ProgramDetailUiState,
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val program = uiState.program ?: return
    val detail = uiState.detail
    val location = detail?.location?.takeIf(String::isNotBlank) ?: program.countryName
    val tuitionDisplay = detail?.tuition?.display?.takeIf(String::isNotBlank)
        ?: program.tuition?.let { NumberFormat.getCurrencyInstance(Locale.US).format(it) }
    val facts = listOfNotNull(
        tuitionDisplay?.let { "Tuition" to it },
        (detail?.duration ?: program.duration)?.takeIf(String::isNotBlank)?.let { "Duration" to it },
        detail?.language?.takeIf(String::isNotBlank)?.let { "Language" to it },
        detail?.studyModeLabel?.takeIf(String::isNotBlank)?.let { "Study mode" to it },
        detail?.deliveryModeLabel?.takeIf(String::isNotBlank)?.let { "Delivery" to it },
        program.degreeLevel.takeIf(String::isNotBlank)?.let { "Degree level" to it },
        (detail?.subject?.name ?: program.subject)?.takeIf(String::isNotBlank)?.let { "Subject" to it },
        location.takeIf(String::isNotBlank)?.let { "Location" to it }
    )
    val intakes = detail?.intakes.orEmpty().mapNotNull { intake ->
        listOfNotNull(
            intake.name?.takeIf(String::isNotBlank),
            intake.intake?.takeIf(String::isNotBlank),
            (intake.deadlineText ?: intake.deadline)?.takeIf(String::isNotBlank)?.let { "Deadline: $it" }
        ).joinToString(" · ").takeIf(String::isNotBlank)
    }.ifEmpty {
        listOfNotNull(
            detail?.nextIntake?.takeIf(String::isNotBlank)?.let { "Next intake: $it" },
            detail?.nextDeadline?.takeIf(String::isNotBlank)?.let { "Next deadline: $it" }
        )
    }

    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            DetailHero(
                title = program.title,
                subtitle = program.universityName,
                location = location,
                badges = listOfNotNull(
                    program.degreeLevel.takeIf(String::isNotBlank),
                    detail?.language?.takeIf(String::isNotBlank),
                    detail?.studyModeLabel?.takeIf(String::isNotBlank),
                    detail?.deliveryModeLabel?.takeIf(String::isNotBlank)
                )
            )
        }
        item {
            ProgramSaveButton(
                isSaved = uiState.isSaved,
                isLoading = uiState.isSavePending,
                onClick = onSaveClick
            )
        }
        item {
            DetailActionRow(
                actions = listOfNotNull(
                    detail?.officialUrl?.let { "Official program page" to it },
                    detail?.applicationUrl?.let { "Apply on university website" to it }
                )
            )
        }
        item { KeyFactsGrid(facts) }
        if (intakes.isNotEmpty()) item { DetailTextSection("Intakes and deadlines", intakes.joinToString("\n")) }
        detail?.tuition?.let { tuition ->
            listOfNotNull(tuition.detail, tuition.notes).filter(String::isNotBlank).joinToString("\n\n").takeIf(String::isNotBlank)?.let { body ->
                item { DetailTextSection("Tuition and fees", body) }
            }
        }
        detail?.admissionRequirements?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Admission requirements", it) } }
        detail?.englishRequirements?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("English-language requirements", it) } }
        detail?.gpaRequirements?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Academic and GPA requirements", it) } }
        detail?.documents?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Required documents", it) } }
        detail?.curriculumSummary?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("Curriculum", it) } }
        detail?.careerOutcomes?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("Career outcomes", it) } }
        detail?.university?.name?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("University", it) } }
        item { SourceStatusSection(detail?.verificationStatus, detail?.lastVerifiedAt) }
    }
}
