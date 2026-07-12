package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun CountryDetailScreen(
    navKey: com.example.degreewiki.ui.navigation.CountryDetail,
    onBackClick: () -> Unit,
    onProgramClick: (String) -> Unit,
    onUniversityClick: (String) -> Unit
) {
    val viewModel = hiltViewModel<CountryDetailViewModel, CountryDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(navKey) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { DetailTopBar(title = "Study destination", onBackClick = onBackClick) }) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding), "Loading destination details")
            uiState.country == null -> DetailUnavailableState(
                title = "Destination unavailable",
                message = "We could not load this destination right now. Go back and try opening it again.",
                actionLabel = "Go back",
                onActionClick = onBackClick,
                modifier = Modifier.padding(innerPadding)
            )
            else -> CountryDetailContent(
                uiState = uiState,
                onProgramClick = onProgramClick,
                onUniversityClick = onUniversityClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
internal fun CountryDetailContent(
    uiState: CountryDetailUiState,
    onProgramClick: (String) -> Unit,
    onUniversityClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val country = uiState.country ?: return
    val detail = uiState.detail
    val overview = detail?.overview?.takeIf(String::isNotBlank) ?: country.summary?.takeIf(String::isNotBlank)
    val heroSummary = detail?.destinationSummary
        ?.takeIf(String::isNotBlank)
        ?.takeUnless { summary -> overview?.trim()?.equals(summary.trim(), ignoreCase = true) == true }
    val currency = listOfNotNull(detail?.currencyName, detail?.currencyCode).filter(String::isNotBlank).distinct().joinToString(" · ")
    val facts = listOfNotNull(
        detail?.iso2?.takeIf(String::isNotBlank)?.let { "Country code" to it },
        detail?.continent?.takeIf(String::isNotBlank)?.let { "Continent" to it },
        currency.takeIf(String::isNotBlank)?.let { "Currency" to it },
        detail?.officialLanguages?.takeIf(List<*>::isNotEmpty)?.let { "Languages" to it.joinToString() }
    )

    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            DetailHero(
                title = country.name,
                location = listOfNotNull(detail?.continent, currency.takeIf(String::isNotBlank)).filter(String::isNotBlank).joinToString(" · "),
                showInitials = true,
                summary = heroSummary
            )
        }
        item { KeyFactsGrid(facts) }
        detail?.tuitionOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Tuition costs", it) } }
        detail?.livingCostOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Living costs", it) } }
        detail?.admissionOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Admissions and education system", it) } }
        detail?.universitySystemOverview?.takeIf(String::isNotBlank)?.let { item { ExpandableTextSection("University system", it) } }
        detail?.requiredDocumentsOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Required documents", it) } }
        detail?.intakeOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Intakes", it) } }
        detail?.visaOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Visa guidance", it) } }
        detail?.studentWorkRightsOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Work while studying", it) } }
        detail?.postStudyWorkOverview?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Post-study options", it) } }
        detail?.scholarshipSummary?.takeIf(String::isNotBlank)?.let { item { DetailTextSection("Scholarships", it) } }
        if (uiState.relatedUniversities.isNotEmpty()) {
            item {
                DetailSection("Universities in ${country.name}") {
                    uiState.relatedUniversities.forEachIndexed { index, university ->
                        if (index > 0) HorizontalDivider()
                        RelatedContentRow(
                            title = university.name,
                            subtitle = university.city,
                            onClick = { onUniversityClick(university.id) }
                        )
                    }
                }
            }
        }
        if (uiState.relatedPrograms.isNotEmpty()) {
            item {
                DetailSection("Programs in ${country.name}") {
                    uiState.relatedPrograms.forEachIndexed { index, program ->
                        if (index > 0) HorizontalDivider()
                        RelatedContentRow(
                            title = program.title,
                            subtitle = listOf(program.universityName, program.degreeLevel).filter(String::isNotBlank).joinToString(" · "),
                            onClick = { onProgramClick(program.id) }
                        )
                    }
                }
            }
        }
        overview?.let { item { ExpandableTextSection("About studying in ${country.name}", it) } }
        detail?.faq?.filter { !it.question.isNullOrBlank() && !it.answer.isNullOrBlank() }?.takeIf(List<*>::isNotEmpty)?.let { faq ->
            item {
                DetailSection("Frequently asked questions") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        faq.forEach { item -> FaqAccordionItem(item.question!!, item.answer!!) }
                    }
                }
            }
        }
        item {
            DetailActionRow(
                actions = listOfNotNull(
                    detail?.officialEducationUrl?.let { "Official education source" to it },
                    detail?.officialVisaUrl?.let { "Official visa information" to it }
                )
            )
        }
        item { SourceStatusSection(detail?.verificationStatus, detail?.lastVerifiedAt) }
    }
}
