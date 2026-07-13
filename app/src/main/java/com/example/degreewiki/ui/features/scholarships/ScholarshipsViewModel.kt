package com.example.degreewiki.ui.features.scholarships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.ui.features.discover.DiscoveryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ScholarshipsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    val uiState: StateFlow<DiscoveryUiState<Scholarship>> = combine(repository.scholarships, repository.scholarshipRefreshState) { items, refresh ->
        when {
            items.isEmpty() && refresh.isRefreshing -> DiscoveryUiState.Loading
            items.isEmpty() && refresh.lastRefreshFailed -> DiscoveryUiState.Error
            else -> DiscoveryUiState.Success(items, items.isNotEmpty() && refresh.lastRefreshFailed, refresh.isRefreshing)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DiscoveryUiState.Loading)

    init { refresh() }
    fun refresh() { viewModelScope.launch { repository.refreshScholarships() } }
}
