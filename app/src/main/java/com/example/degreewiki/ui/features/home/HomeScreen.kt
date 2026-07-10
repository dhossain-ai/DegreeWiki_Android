package com.example.degreewiki.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.DeferredFeatureCard
import com.example.degreewiki.ui.components.PrimaryActionButton
import com.example.degreewiki.ui.components.QuickBrowseCard
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.ScreenHero
import com.example.degreewiki.ui.components.SearchEntryCard
import com.example.degreewiki.ui.components.SectionHeader
import com.example.degreewiki.ui.components.StatusBadge
import com.example.degreewiki.ui.components.StatusBadgeTone
import com.example.degreewiki.ui.components.TrustNote
import com.example.degreewiki.ui.components.ErrorState

@Composable
fun HomeScreen(
    onProgramsClick: () -> Unit,
    onUniversitiesClick: () -> Unit,
    onDestinationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.showFullError) {
        ErrorState(
            title = "Study-abroad data unavailable",
            message = "We could not load study-abroad data right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        return
    }

    DegreeWikiScreen(modifier = modifier) {
        item {
            ScreenHero(
                title = "DegreeWiki",
                subtitle = "Find and compare degrees abroad with a clearer, student-first starting point."
            )
        }
        item {
            SearchEntryCard(
                title = "Start with real browse data",
                helper = "Open the existing Programs browse flow to search with the data already available in the app.",
                onClick = onProgramsClick
            )
        }
        item {
            TrustNote(
                text = "Source-aware study-abroad data. Always confirm final details on the official university or scholarship page before applying."
            )
        }
        if (state.showRefreshWarning) {
            item {
                RefreshWarningNote(
                    text = "Showing saved information. We could not refresh right now.",
                    actionLabel = "Retry",
                    onActionClick = viewModel::refresh
                )
            }
        }
        item {
            SectionHeader(
                title = "Quick browse",
                subtitle = "Jump into the core public catalog."
            )
        }
        item {
            QuickBrowseCard(
                title = "Programs",
                description = "Browse real program records already synced from the API.",
                meta = "${state.programs.size} cached programs",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                onClick = onProgramsClick
            )
        }
        item {
            QuickBrowseCard(
                title = "Universities",
                description = "Explore institutions and open their safe cached detail views.",
                meta = "${state.universities.size} cached universities",
                icon = Icons.Default.School,
                onClick = onUniversitiesClick
            )
        }
        item {
            QuickBrowseCard(
                title = "Destinations",
                description = "Browse available countries and destination summaries.",
                meta = "${state.countries.size} cached destinations",
                icon = Icons.Default.LocationOn,
                onClick = onDestinationsClick
            )
        }
        item {
            SectionHeader(
                title = "Featured now",
                subtitle = "Only real API-backed content is shown here."
            )
        }
        item {
            HomeFeaturedCard(
                title = state.programs.firstOrNull()?.title ?: "Programs update as soon as sync completes",
                body = state.programs.firstOrNull()?.let {
                    "${it.universityName} • ${it.countryName}"
                } ?: "Open Programs to browse the currently available catalog.",
                badge = if (state.programs.isNotEmpty()) "Real data" else "Waiting for data"
            )
        }
        state.universities.firstOrNull()?.let { university ->
            item {
                HomeFeaturedCard(
                    title = university.name,
                    body = listOfNotNull(university.city, university.overview).joinToString(" • ").ifBlank {
                        "Available in the Universities catalog."
                    },
                    badge = "University"
                )
            }
        }
        item {
            SectionHeader(
                title = "Next tools",
                subtitle = "Helpful surfaces we can add safely later."
            )
        }
        item {
            DegreeWikiCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Fit Finder",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        StatusBadge(label = "Soon", tone = StatusBadgeTone.Deadline)
                    }
                    Text(
                        text = "Not sure where to start? Fit Finder will help match programs to your goals.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    PrimaryActionButton(
                        text = "Sign in to follow Fit Finder updates",
                        onClick = onProfileClick
                    )
                }
            }
        }
        item {
            DeferredFeatureCard(
                title = "Ask a study-abroad question",
                description = "A small help entry can live here later, but this Android shell does not present chat as a main working feature yet."
            )
        }
        item {
            DeferredFeatureCard(
                title = "Scholarships and guides",
                description = "Future public content can be linked from Home once safe Android routes exist."
            )
        }
    }
}

@Composable
private fun HomeFeaturedCard(
    title: String,
    body: String,
    badge: String
) {
    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StatusBadge(label = badge, tone = StatusBadgeTone.Neutral)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
