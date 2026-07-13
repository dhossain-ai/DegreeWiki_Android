package com.example.degreewiki.ui.features.guides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.GuideDetail
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.LoadingState
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.formatPublicDate
import com.example.degreewiki.ui.features.details.DetailHero
import com.example.degreewiki.ui.features.details.DetailSection
import com.example.degreewiki.ui.features.details.DetailTopBar
import com.example.degreewiki.ui.features.details.DetailUnavailableState
import com.example.degreewiki.ui.features.details.RelatedContentRow
import com.example.degreewiki.ui.features.details.SourceStatusSection
import com.example.degreewiki.ui.navigation.GuideDetail as GuideDetailKey

@Composable
fun GuideDetailScreen(navKey: GuideDetailKey, onBackClick: () -> Unit, onRelatedGuideClick: (String) -> Unit) {
    val viewModel = hiltViewModel<GuideDetailViewModel, GuideDetailViewModel.Factory>(creationCallback = { it.create(navKey) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(topBar = { DetailTopBar("Study guide", onBackClick) }) { padding ->
        when {
            state.isLoading && state.cached == null && state.detail == null -> LoadingState(Modifier.padding(padding), "Loading study guide")
            state.cached == null && state.detail == null -> DetailUnavailableState("Guide unavailable", "We could not load this guide right now.", "Retry", viewModel::loadDetail, Modifier.padding(padding))
            else -> GuideDetailContent(state.detail?.summary ?: state.cached!!, state.detail, state.detailFailed, onRelatedGuideClick, Modifier.padding(padding))
        }
    }
}

@Composable
internal fun GuideDetailContent(summary: Guide, detail: GuideDetail?, detailFailed: Boolean, onRelatedGuideClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val publication = formatPublicDate(summary.publishedAt)
    val meta = listOfNotNull(publication, detail?.readTimeMinutes?.let { "$it min read" }).joinToString(" · ").takeIf(String::isNotBlank)
    DegreeWikiScreen(modifier, verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            DetailHero(
                title = summary.title,
                subtitle = summary.category?.name,
                location = meta,
                summary = summary.summary
            )
        }
        if (detailFailed && detail == null) item { RefreshWarningNote("The full guide could not be refreshed. Try again when you are online.") }
        detail?.body?.takeIf(List<*>::isNotEmpty)?.let { blocks -> item { GuideContentRenderer(blocks) } }
        detail?.relatedGuides?.takeIf(List<*>::isNotEmpty)?.let { related ->
            item {
                DetailSection("Related guides") {
                    related.forEach { guide ->
                        RelatedContentRow(guide.title, guide.category?.name ?: formatPublicDate(guide.publishedAt), { onRelatedGuideClick(guide.slug) })
                    }
                }
            }
        }
        detail?.let { item { SourceStatusSection(it.verificationStatus, it.lastVerifiedAt) } }
    }
}
