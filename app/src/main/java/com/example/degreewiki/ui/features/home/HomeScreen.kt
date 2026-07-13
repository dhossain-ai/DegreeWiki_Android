package com.example.degreewiki.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.ui.components.CountryBrowseCard
import com.example.degreewiki.ui.components.DeferredFeatureCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.HomeSearchCard
import com.example.degreewiki.ui.components.HorizontalContentSection
import com.example.degreewiki.ui.components.ProgramBrowseCard
import com.example.degreewiki.ui.components.QuickBrowseCard
import com.example.degreewiki.ui.components.RefreshWarningNote
import com.example.degreewiki.ui.components.SectionHeader
import com.example.degreewiki.ui.components.TrustNote
import com.example.degreewiki.ui.components.UniversityBrowseCard

@Composable
fun HomeScreen(
    onProgramsClick: (String) -> Unit,
    onUniversitiesClick: () -> Unit,
    onDestinationsClick: () -> Unit,
    onScholarshipsClick: () -> Unit,
    onGuidesClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.showFullError) {
        ErrorState(
            title = "Study-abroad information unavailable",
            message = "We could not load study-abroad information right now. Check your connection and try again.",
            actionLabel = "Retry",
            onActionClick = viewModel::refresh,
            modifier = modifier
        )
        return
    }

    HomeContent(
        state = state,
        onProgramsClick = onProgramsClick,
        onUniversitiesClick = onUniversitiesClick,
        onDestinationsClick = onDestinationsClick,
        onScholarshipsClick = onScholarshipsClick,
        onGuidesClick = onGuidesClick,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onProgramsClick: (String) -> Unit,
    onUniversitiesClick: () -> Unit,
    onDestinationsClick: () -> Unit,
    onScholarshipsClick: () -> Unit,
    onGuidesClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    DegreeWikiScreen(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            androidx.compose.foundation.layout.Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "DegreeWiki",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Find degrees abroad",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Search programs, universities, and study destinations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            HomeSearchCard(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { onProgramsClick(searchQuery.trim()) }
            )
        }
        if (state.showRefreshWarning) {
            item {
                RefreshWarningNote(
                    text = "Showing saved information. We could not refresh right now.",
                    actionLabel = "Retry",
                    onActionClick = onRefresh
                )
            }
        }
        item {
            SectionHeader(
                title = "Explore your options",
                subtitle = "Start with degrees, universities, or a study destination."
            )
        }
        item {
            QuickBrowseCard(
                title = "Programs",
                description = "Compare degrees by tuition, duration, and language.",
                meta = naturalCount(state.programs.size, "program", "programs"),
                icon = Icons.AutoMirrored.Filled.MenuBook,
                onClick = { onProgramsClick("") }
            )
        }
        item {
            QuickBrowseCard(
                title = "Scholarships",
                description = "Explore funding opportunities for international study.",
                meta = "Browse scholarships",
                icon = Icons.Default.CardGiftcard,
                onClick = onScholarshipsClick
            )
        }
        item {
            QuickBrowseCard(
                title = "Guides",
                description = "Read practical guidance for studying abroad.",
                meta = "Browse study guides",
                icon = Icons.AutoMirrored.Filled.Article,
                onClick = onGuidesClick
            )
        }
        item {
            QuickBrowseCard(
                title = "Universities",
                description = "Explore institutions and their programs.",
                meta = naturalCount(state.universities.size, "university", "universities"),
                icon = Icons.Default.School,
                onClick = onUniversitiesClick
            )
        }
        item {
            QuickBrowseCard(
                title = "Countries",
                description = "Plan where to study abroad.",
                meta = naturalCount(state.countries.size, "country", "countries"),
                icon = Icons.Default.LocationOn,
                onClick = onDestinationsClick
            )
        }
        if (state.programs.isNotEmpty()) {
            item {
                HorizontalContentSection(
                    title = "Featured programs",
                    items = state.programs.take(3),
                    key = { it.id }
                ) { program, cardModifier ->
                    ProgramBrowseCard(
                        program = program,
                        onClick = { onProgramsClick("") },
                        modifier = cardModifier
                    )
                }
            }
        }
        if (state.universities.isNotEmpty()) {
            item {
                HorizontalContentSection(
                    title = "Popular universities",
                    items = state.universities.take(3),
                    key = { it.id }
                ) { university, cardModifier ->
                    UniversityBrowseCard(
                        university = university,
                        onClick = onUniversitiesClick,
                        modifier = cardModifier
                    )
                }
            }
        }
        if (state.countries.isNotEmpty()) {
            item {
                HorizontalContentSection(
                    title = "Study destinations",
                    items = state.countries.take(3),
                    key = { it.id }
                ) { country, cardModifier ->
                    CountryBrowseCard(
                        country = country,
                        onClick = onDestinationsClick,
                        modifier = cardModifier
                    )
                }
            }
        }
        item {
            DeferredFeatureCard(
                title = "Not sure where to start?",
                description = "Fit Finder will help you explore programs based on your goals."
            )
        }
        item {
            TrustNote(
                text = "DegreeWiki is independent. Always confirm final details on official university or scholarship pages."
            )
        }
    }
}

private fun naturalCount(count: Int, singular: String, plural: String): String =
    "$count ${if (count == 1) singular else plural}"
