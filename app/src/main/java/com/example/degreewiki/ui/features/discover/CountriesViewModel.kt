package com.example.degreewiki.ui.features.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<DiscoveryUiState<Country>> =
        combine(
            dataRepository.countries,
            dataRepository.countryRefreshState
        ) { countries, refreshState ->
            when {
                countries.isEmpty() && refreshState.isRefreshing -> DiscoveryUiState.Loading
                countries.isEmpty() && refreshState.lastRefreshFailed -> DiscoveryUiState.Error
                else -> DiscoveryUiState.Success(
                    data = countries,
                    showRefreshWarning = countries.isNotEmpty() && refreshState.lastRefreshFailed,
                    isRefreshing = refreshState.isRefreshing
                )
            }
        }
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
            dataRepository.refreshCountries()
        }
    }
}
