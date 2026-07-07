package com.example.degreewiki.ui.features.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversitiesViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<DiscoveryUiState<University>> =
        dataRepository.universities
            .map<List<University>, DiscoveryUiState<University>>(DiscoveryUiState::Success)
            .catch { emit(DiscoveryUiState.Error(it)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DiscoveryUiState.Loading
            )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            dataRepository.refreshUniversities()
        }
    }
}
