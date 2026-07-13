package com.example.degreewiki.ui.features.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CountriesBrowseUiState(
    val discovery: DiscoveryUiState<Country> = DiscoveryUiState.Loading,
    val query: String = "",
    val results: List<Country> = emptyList(),
    val totalCount: Int = 0
)

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<CountriesBrowseUiState> = combine(
        dataRepository.countries,
        dataRepository.countryRefreshState,
        query
    ) { countries, refreshState, currentQuery ->
        val discovery = when {
            countries.isEmpty() && refreshState.isRefreshing -> DiscoveryUiState.Loading
            countries.isEmpty() && refreshState.lastRefreshFailed -> DiscoveryUiState.Error
            else -> DiscoveryUiState.Success(countries, countries.isNotEmpty() && refreshState.lastRefreshFailed, refreshState.isRefreshing)
        }
        CountriesBrowseUiState(discovery, currentQuery, filterCountries(countries, currentQuery), countries.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CountriesBrowseUiState())

    init { refresh() }
    fun setQuery(value: String) { query.value = value }
    fun clearSearch() { query.value = "" }
    fun refresh() { viewModelScope.launch { dataRepository.refreshCountries() } }
}
