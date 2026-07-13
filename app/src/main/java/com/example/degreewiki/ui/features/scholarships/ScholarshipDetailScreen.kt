package com.example.degreewiki.ui.features.scholarships

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.domain.model.ScholarshipDetail
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.features.details.DetailActionRow
import com.example.degreewiki.ui.features.details.DetailHero
import com.example.degreewiki.ui.features.details.DetailTextSection
import com.example.degreewiki.ui.features.details.DetailTopBar
import com.example.degreewiki.ui.features.details.DetailUnavailableState
import com.example.degreewiki.ui.features.details.ExpandableTextSection
import com.example.degreewiki.ui.features.details.KeyFactsGrid
import com.example.degreewiki.ui.features.details.SourceStatusSection
import com.example.degreewiki.ui.navigation.ScholarshipDetail as ScholarshipDetailKey

@Composable
fun ScholarshipDetailScreen(navKey: ScholarshipDetailKey, onBackClick: () -> Unit) {
    val viewModel = hiltViewModel<ScholarshipDetailViewModel, ScholarshipDetailViewModel.Factory>(creationCallback = { it.create(navKey) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(topBar = { DetailTopBar("Scholarship details", onBackClick) }) { padding ->
        when {
            state.isLoading && state.cached == null && state.detail == null -> LoadingState(Modifier.padding(padding), "Loading scholarship details")
            state.cached == null && state.detail == null -> DetailUnavailableState("Scholarship unavailable", "We could not load this scholarship right now.", "Retry", viewModel::loadDetail, Modifier.padding(padding))
            else -> ScholarshipDetailContent(state.detail?.summary ?: state.cached!!, state.detail, Modifier.padding(padding))
        }
    }
}

@Composable
internal fun ScholarshipDetailContent(summary: Scholarship, detail: ScholarshipDetail?, modifier: Modifier = Modifier) {
    val deadline = scholarshipDeadlinePresentation(summary.deadline, summary.deadlineDisplay, summary.deadlineText)
    val facts = listOfNotNull(
        summary.amountDisplay?.let { "Funding" to it },
        deadline?.label?.let { "Deadline" to it.removePrefix("Deadline ") },
        summary.studyCountries.takeIf(List<String>::isNotEmpty)?.joinToString()?.let { "Study countries" to it },
        summary.degreeLevels.takeIf(List<String>::isNotEmpty)?.joinToString()?.let { "Degree levels" to it },
        summary.subjects.takeIf(List<String>::isNotEmpty)?.joinToString()?.let { "Subjects" to it },
        detail?.eligibleNationalities?.map { it.country.name }?.takeIf(List<String>::isNotEmpty)?.joinToString()?.let { "Eligible nationalities" to it }
    )
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            DetailHero(
                title = summary.title,
                subtitle = summary.providerName,
                location = deadline?.label,
                badges = listOfNotNull(summary.scholarshipTypeLabel, summary.fundingTypeLabel),
                summary = summary.summary
            )
        }
        item { DetailActionRow(listOfNotNull(summary.officialUrl?.let { "View official scholarship" to it }, summary.applicationUrl?.let { "Apply on official website" to it })) }
        if (facts.isNotEmpty()) item { KeyFactsGrid(facts) }
        detail?.overview?.let { item { ExpandableTextSection("Overview", it) } }
        detail?.coverageNotes?.let { item { DetailTextSection("Funding coverage", it) } }
        detail?.eligibilitySummary?.let { item { ExpandableTextSection("Eligibility", it) } }
        detail?.eligibleNationalities?.takeIf(List<*>::isNotEmpty)?.let { rows ->
            item { DetailTextSection("Eligible nationalities", rows.joinToString("\n") { listOfNotNull(it.country.name, it.type?.replace('_', ' '), it.notes).joinToString(" · ") }) }
        }
        detail?.studyCountries?.takeIf(List<*>::isNotEmpty)?.let { item { DetailTextSection("Eligible countries", it.joinToString { country -> country.name }) } }
        val levelSubjectText = listOfNotNull(
            detail?.degreeLevels?.takeIf(List<*>::isNotEmpty)?.joinToString { it.name }?.let { "Degree levels: $it" },
            detail?.subjects?.takeIf(List<*>::isNotEmpty)?.joinToString { it.name }?.let { "Subjects: $it" }
        ).joinToString("\n")
        if (levelSubjectText.isNotBlank()) item { DetailTextSection("Degree levels and subjects", levelSubjectText) }
        detail?.universities?.takeIf(List<*>::isNotEmpty)?.let { item { DetailTextSection("Universities", it.joinToString("\n") { university -> university.name }) } }
        detail?.programs?.takeIf(List<*>::isNotEmpty)?.let { item { DetailTextSection("Programs", it.joinToString("\n") { program -> program.title }) } }
        summary.deadlineText?.takeIf { it != summary.deadlineDisplay }?.let { item { DetailTextSection("Deadline information", it) } }
        item { SourceStatusSection(summary.verificationStatus, summary.lastVerifiedAt) }
    }
}
