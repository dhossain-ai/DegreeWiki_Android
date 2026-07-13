package com.example.degreewiki.ui.features.scholarships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.domain.model.ScholarshipDetail
import com.example.degreewiki.ui.navigation.ScholarshipDetail as ScholarshipDetailKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScholarshipDetailUiState(
    val cached: Scholarship? = null,
    val detail: ScholarshipDetail? = null,
    val isLoading: Boolean = true,
    val detailFailed: Boolean = false
)

@HiltViewModel(assistedFactory = ScholarshipDetailViewModel.Factory::class)
class ScholarshipDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: ScholarshipDetailKey,
    private val repository: DataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScholarshipDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getScholarshipBySlug(navKey.slug).collectLatest { cached ->
                _uiState.update { it.copy(cached = cached) }
            }
        }
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, detailFailed = false) }
            val detail = repository.getScholarshipDetail(navKey.slug)
            _uiState.update { it.copy(detail = detail, isLoading = false, detailFailed = detail == null) }
        }
    }

    @AssistedFactory interface Factory { fun create(navKey: ScholarshipDetailKey): ScholarshipDetailViewModel }
}
