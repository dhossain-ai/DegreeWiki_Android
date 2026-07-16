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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.degreewiki.ui.components.LoginToSavePrompt
import com.example.degreewiki.data.repository.SaveProgramResult
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment

@Composable
fun HomeScreen(
    onProgramsClick: (String) -> Unit,
    onUniversitiesClick: () -> Unit,
    onDestinationsClick: () -> Unit,
    onScholarshipsClick: () -> Unit,
    onGuidesClick: () -> Unit,
    onLoginRequired: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLoginPrompt by rememberSaveable { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.saveEvents.collect { result ->
            when (result) {
                SaveProgramResult.LoginRequired -> showLoginPrompt = true
                is SaveProgramResult.Failure -> snackbar.showSnackbar(result.message)
                SaveProgramResult.Success -> Unit
            }
        }
    }

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
        onSaveClick = viewModel::toggleSaved,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )

    Box(modifier = modifier) {
        SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
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
fun HomeContent(
    state: HomeUiState,
    onProgramsClick: (String) -> Unit,
    onUniversitiesClick: () -> Unit,
    onDestinationsClick: () -> Unit,
    onScholarshipsClick: () -> Unit,
    onGuidesClick: () -> Unit,
    onSaveClick: (com.example.degreewiki.domain.model.Program) -> Unit = {},
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
                        isSaved = program.id in state.savedProgramIds,
                        isSaveLoading = program.id in state.pendingProgramIds,
                        onSaveClick = { onSaveClick(program) },
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
