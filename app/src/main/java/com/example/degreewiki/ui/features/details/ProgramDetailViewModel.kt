package com.example.degreewiki.ui.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.ui.navigation.ProgramDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProgramDetailUiState(
    val program: Program? = null,
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = ProgramDetailViewModel.Factory::class)
class ProgramDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: ProgramDetail,
    dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<ProgramDetailUiState> = dataRepository.getProgramById(navKey.id)
        .map { ProgramDetailUiState(program = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProgramDetailUiState(isLoading = true)
        )

    @AssistedFactory
    interface Factory {
        fun create(navKey: ProgramDetail): ProgramDetailViewModel
    }
}
