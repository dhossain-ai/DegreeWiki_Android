package com.example.degreewiki.ui.features.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.data.repository.ProfileRepository
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.domain.model.Program
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class ProgramsBrowseUiState(
    val discovery: DiscoveryUiState<Program> = DiscoveryUiState.Loading,
    val search: ProgramSearchState = ProgramSearchState(),
    val results: List<Program> = emptyList(),
    val filterOptions: ProgramFilterOptions = ProgramFilterOptions(),
    val totalCount: Int = 0,
    val savedProgramIds: Set<String> = emptySet(),
    val pendingProgramIds: Set<String> = emptySet()
)

@HiltViewModel
class ProgramsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val searchState = MutableStateFlow(ProgramSearchState())
    private val discoveryState = combine(
        dataRepository.programs,
        dataRepository.programRefreshState
    ) { programs, refreshState ->
        when {
            programs.isEmpty() && refreshState.isRefreshing -> DiscoveryUiState.Loading
            programs.isEmpty() && refreshState.lastRefreshFailed -> DiscoveryUiState.Error
            else -> DiscoveryUiState.Success(
                data = programs,
                showRefreshWarning = programs.isNotEmpty() && refreshState.lastRefreshFailed,
                isRefreshing = refreshState.isRefreshing
            )
        }
    }

    private val _saveEvents = MutableSharedFlow<SaveProgramResult>()
    val saveEvents = _saveEvents.asSharedFlow()

    val uiState: StateFlow<ProgramsBrowseUiState> = combine(
        discoveryState,
        searchState,
        profileRepository.savedProgramsState
    ) { discovery, search, savedState ->
        val programs = (discovery as? DiscoveryUiState.Success<Program>)?.data.orEmpty()
        ProgramsBrowseUiState(
            discovery = discovery,
            search = search,
            results = filterPrograms(programs, search),
            filterOptions = programFilterOptions(programs),
            totalCount = programs.size,
            savedProgramIds = savedState.savedItemIdsByProgramId.keys,
            pendingProgramIds = savedState.pendingProgramIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProgramsBrowseUiState()
    )

    init { refresh() }

    fun setQuery(query: String) = searchState.update { it.copy(query = query) }
    fun setFilters(filters: ProgramFilters) = searchState.update { it.copy(filters = filters) }
    fun removeFilter(value: String) = searchState.update { it.copy(filters = it.filters.remove(value)) }
    fun clearFilters() = searchState.update { it.copy(filters = ProgramFilters()) }
    fun clearSearch() = searchState.update { it.copy(query = "") }
    fun clearAll() = searchState.update { ProgramSearchState() }
    fun setSort(sort: ProgramSortOption) = searchState.update { it.copy(sort = sort) }

    fun refresh() {
        viewModelScope.launch { dataRepository.refreshPrograms() }
    }

    fun toggleSaved(program: Program) {
        viewModelScope.launch {
            _saveEvents.emit(profileRepository.toggleSavedProgram(program))
        }
    }
}
