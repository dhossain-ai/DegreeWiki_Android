package com.example.degreewiki.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.data.repository.ProfileRepository
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.ui.navigation.ProgramDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.degreewiki.data.network.dto.ProgramDetailDto
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class ProgramDetailUiState(
    val program: Program? = null,
    val isLoading: Boolean = true
    ,val detail: ProgramDetailDto? = null,
    val isSaved: Boolean = false,
    val isSavePending: Boolean = false
)

@HiltViewModel(assistedFactory = ProgramDetailViewModel.Factory::class)
class ProgramDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: ProgramDetail,
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProgramDetailUiState())
    val uiState = _uiState.asStateFlow()
    private val _saveEvents = MutableSharedFlow<SaveProgramResult>()
    val saveEvents = _saveEvents.asSharedFlow()
    init { viewModelScope.launch { dataRepository.getProgramById(navKey.id).collectLatest { cached ->
        _uiState.value = _uiState.value.copy(program = cached, isLoading = cached == null)
        val detail = cached?.slug?.takeIf(String::isNotBlank)?.let { dataRepository.getProgramDetail(it) }
        _uiState.value = _uiState.value.copy(detail = detail, isLoading = false)
    } }
        viewModelScope.launch {
            profileRepository.savedProgramsState.collectLatest { savedState ->
                _uiState.value = _uiState.value.copy(
                    isSaved = savedState.isSaved(navKey.id),
                    isSavePending = savedState.isPending(navKey.id)
                )
            }
        }
    }

    fun toggleSaved() {
        val program = _uiState.value.program ?: return
        viewModelScope.launch {
            _saveEvents.emit(profileRepository.toggleSavedProgram(program))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: ProgramDetail): ProgramDetailViewModel
    }
}
