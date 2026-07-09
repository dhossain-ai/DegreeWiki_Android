package com.example.degreewiki.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val programs: List<Program> = emptyList(),
    val universities: List<University> = emptyList(),
    val countries: List<Country> = emptyList(),
    val isRefreshing: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        dataRepository.programs,
        dataRepository.universities,
        dataRepository.countries
    ) { programs, universities, countries ->
        HomeUiState(
            programs = programs,
            universities = universities,
            countries = countries,
            isRefreshing = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            dataRepository.refreshPrograms()
            dataRepository.refreshUniversities()
            dataRepository.refreshCountries()
        }
    }
}
