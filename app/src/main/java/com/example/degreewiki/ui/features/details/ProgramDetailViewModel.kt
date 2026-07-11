package com.example.degreewiki.ui.features.details

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.degreewiki.data.network.dto.ProgramDetailDto
import kotlinx.coroutines.flow.stateIn

data class ProgramDetailUiState(
    val program: Program? = null,
    val isLoading: Boolean = true
    ,val detail: ProgramDetailDto? = null
)

@HiltViewModel(assistedFactory = ProgramDetailViewModel.Factory::class)
class ProgramDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: ProgramDetail,
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProgramDetailUiState())
    val uiState = _uiState.asStateFlow()
    init { viewModelScope.launch { dataRepository.getProgramById(navKey.id).collectLatest { cached ->
        _uiState.value = _uiState.value.copy(program = cached, isLoading = cached == null)
        val detail = cached?.slug?.takeIf(String::isNotBlank)?.let { dataRepository.getProgramDetail(it) }
        _uiState.value = _uiState.value.copy(detail = detail, isLoading = false)
    } } }

    @AssistedFactory
    interface Factory {
        fun create(navKey: ProgramDetail): ProgramDetailViewModel
    }
}
