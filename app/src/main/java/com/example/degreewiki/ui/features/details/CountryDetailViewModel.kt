package com.example.degreewiki.ui.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.ui.navigation.CountryDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CountryDetailUiState(
    val country: Country? = null,
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = CountryDetailViewModel.Factory::class)
class CountryDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: CountryDetail,
    dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<CountryDetailUiState> = dataRepository.getCountryById(navKey.id)
        .map { CountryDetailUiState(country = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CountryDetailUiState(isLoading = true)
        )

    @AssistedFactory
    interface Factory {
        fun create(navKey: CountryDetail): CountryDetailViewModel
    }
}
