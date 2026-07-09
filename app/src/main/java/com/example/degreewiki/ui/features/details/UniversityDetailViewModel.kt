package com.example.degreewiki.ui.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.navigation.UniversityDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class UniversityDetailUiState(
    val university: University? = null,
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = UniversityDetailViewModel.Factory::class)
class UniversityDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: UniversityDetail,
    dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<UniversityDetailUiState> = dataRepository.getUniversityById(navKey.id)
        .map { UniversityDetailUiState(university = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UniversityDetailUiState(isLoading = true)
        )

    @AssistedFactory
    interface Factory {
        fun create(navKey: UniversityDetail): UniversityDetailViewModel
    }
}
