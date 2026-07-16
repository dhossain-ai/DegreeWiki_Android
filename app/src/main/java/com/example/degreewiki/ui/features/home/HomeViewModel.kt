package com.example.degreewiki.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.data.repository.ProfileRepository
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class HomeUiState(
    val programs: List<Program> = emptyList(),
    val universities: List<University> = emptyList(),
    val countries: List<Country> = emptyList(),
    val isRefreshing: Boolean = true,
    val showRefreshWarning: Boolean = false,
    val showFullError: Boolean = false,
    val savedProgramIds: Set<String> = emptySet(),
    val pendingProgramIds: Set<String> = emptySet()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _saveEvents = MutableSharedFlow<SaveProgramResult>()
    val saveEvents = _saveEvents.asSharedFlow()

    private val refreshSummary = combine(
        dataRepository.programRefreshState,
        dataRepository.universityRefreshState,
        dataRepository.countryRefreshState
    ) { programRefresh, universityRefresh, countryRefresh ->
        val refreshStates = listOf(programRefresh, universityRefresh, countryRefresh)
        RefreshSummary(
            isRefreshing = refreshStates.any { it.isRefreshing },
            hasRefreshFailure = refreshStates.any { it.lastRefreshFailed }
        )
    }

    val uiState: StateFlow<HomeUiState> = combine(
        dataRepository.programs,
        dataRepository.universities,
        dataRepository.countries,
        refreshSummary,
        profileRepository.savedProgramsState
    ) { programs, universities, countries, refreshSummary, savedState ->
        val hasAnyData = programs.isNotEmpty() || universities.isNotEmpty() || countries.isNotEmpty()

        HomeUiState(
            programs = programs,
            universities = universities,
            countries = countries,
            isRefreshing = refreshSummary.isRefreshing,
            showRefreshWarning = hasAnyData && refreshSummary.hasRefreshFailure,
            showFullError = !hasAnyData && refreshSummary.hasRefreshFailure && !refreshSummary.isRefreshing,
            savedProgramIds = savedState.savedItemIdsByProgramId.keys,
            pendingProgramIds = savedState.pendingProgramIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            dataRepository.refreshPrograms()
            dataRepository.refreshUniversities()
            dataRepository.refreshCountries()
        }
    }

    fun toggleSaved(program: Program) {
        viewModelScope.launch {
            _saveEvents.emit(profileRepository.toggleSavedProgram(program))
        }
    }
}

private data class RefreshSummary(
    val isRefreshing: Boolean,
    val hasRefreshFailure: Boolean
)
