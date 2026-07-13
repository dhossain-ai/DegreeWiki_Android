package com.example.degreewiki.ui.features.guides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.GuideDetail
import com.example.degreewiki.ui.navigation.GuideDetail as GuideDetailKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GuideDetailUiState(val cached: Guide? = null, val detail: GuideDetail? = null, val isLoading: Boolean = true, val detailFailed: Boolean = false)

@HiltViewModel(assistedFactory = GuideDetailViewModel.Factory::class)
class GuideDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: GuideDetailKey,
    private val repository: DataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GuideDetailUiState())
    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch { repository.getGuideBySlug(navKey.slug).collectLatest { cached -> _uiState.update { it.copy(cached = cached) } } }
        loadDetail()
    }
    fun loadDetail() { viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, detailFailed = false) }
        val detail = repository.getGuideDetail(navKey.slug)
        _uiState.update { it.copy(detail = detail, isLoading = false, detailFailed = detail == null) }
    } }
    @AssistedFactory interface Factory { fun create(navKey: GuideDetailKey): GuideDetailViewModel }
}
