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
                    item {
                        DetailFactsCard("Destination facts", facts = buildList {
                            uiState.detail?.iso2?.let { add("Country code" to it) }
                            uiState.detail?.continent?.let { add("Continent" to it) }
                            listOfNotNull(uiState.detail?.currencyName, uiState.detail?.currencyCode).distinct().joinToString(" · ").takeIf { it.isNotBlank() }?.let { add("Currency" to it) }
                            uiState.detail?.capitalCityName?.let { add("Capital" to it) }
                            uiState.detail?.officialLanguages?.takeIf { it.isNotEmpty() }?.let { add("Official languages" to it.joinToString()) }
                        })
                    }
                    listOf(
                        "Tuition overview" to uiState.detail?.tuitionOverview,
                        "Living costs" to uiState.detail?.livingCostOverview,
                        "Admissions" to uiState.detail?.admissionOverview,
                        "Visa guidance" to uiState.detail?.visaOverview,
                        "Student work rights" to uiState.detail?.studentWorkRightsOverview,
                        "Post-study work" to uiState.detail?.postStudyWorkOverview,
                        "University system" to uiState.detail?.universitySystemOverview,
                        "Required documents" to uiState.detail?.requiredDocumentsOverview,
                        "Intakes" to uiState.detail?.intakeOverview,
                        "Official education information" to uiState.detail?.officialEducationUrl,
                        "Official visa information" to uiState.detail?.officialVisaUrl
                    ).forEach { (title, body) -> body?.takeIf { it.isNotBlank() }?.let { item { DetailTextSection(title, it) } } }
                    uiState.detail?.faq?.filter { !it.question.isNullOrBlank() && !it.answer.isNullOrBlank() }?.takeIf { it.isNotEmpty() }?.let { faq ->
                        item { RelatedTextListCard("Frequently asked questions", items = faq.map { "${it.question}\n${it.answer}" }) }
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
