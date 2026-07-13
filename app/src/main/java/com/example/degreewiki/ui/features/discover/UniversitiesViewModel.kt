package com.example.degreewiki.ui.features.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UniversitiesBrowseUiState(
    val discovery: DiscoveryUiState<University> = DiscoveryUiState.Loading,
    val query: String = "",
    val results: List<University> = emptyList(),
    val totalCount: Int = 0
)

@HiltViewModel
class UniversitiesViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<UniversitiesBrowseUiState> = combine(
        dataRepository.universities,
        dataRepository.universityRefreshState,
        query
    ) { universities, refreshState, currentQuery ->
        val discovery = when {
            universities.isEmpty() && refreshState.isRefreshing -> DiscoveryUiState.Loading
            universities.isEmpty() && refreshState.lastRefreshFailed -> DiscoveryUiState.Error
            else -> DiscoveryUiState.Success(universities, universities.isNotEmpty() && refreshState.lastRefreshFailed, refreshState.isRefreshing)
        }
        UniversitiesBrowseUiState(discovery, currentQuery, filterUniversities(universities, currentQuery), universities.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UniversitiesBrowseUiState())

    init { refresh() }
    fun setQuery(value: String) { query.value = value }
    fun clearSearch() { query.value = "" }
    fun refresh() { viewModelScope.launch { dataRepository.refreshUniversities() } }
}
