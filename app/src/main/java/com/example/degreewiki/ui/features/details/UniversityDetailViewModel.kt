package com.example.degreewiki.ui.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class UniversityDetailUiState(
    val university: University? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class UniversityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: DataRepository
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["id"])

    val uiState: StateFlow<UniversityDetailUiState> = dataRepository.getUniversityById(id)
        .map { UniversityDetailUiState(university = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UniversityDetailUiState(isLoading = true)
        )
}
