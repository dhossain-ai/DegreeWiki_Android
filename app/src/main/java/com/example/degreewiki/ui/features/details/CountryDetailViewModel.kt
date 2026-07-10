package com.example.degreewiki.ui.features.details

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CountryDetailUiState(
    val country: Country? = null,
    val relatedUniversities: List<String> = emptyList(),
    val relatedPrograms: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = CountryDetailViewModel.Factory::class)
class CountryDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: CountryDetail,
    dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<CountryDetailUiState> = combine(
        dataRepository.getCountryById(navKey.id),
        dataRepository.universities,
        dataRepository.programs
    ) { country, universities, programs ->
        val relatedUniversities = country?.id
            ?.let { countryId ->
                universities
                    .asSequence()
                    .filter { it.countryId == countryId }
                    .map { it.name }
                    .distinct()
                    .take(4)
                    .toList()
            }
            .orEmpty()

        val relatedPrograms = country?.name
            ?.let { countryName ->
                programs
                    .asSequence()
                    .filter { it.countryName.equals(countryName, ignoreCase = true) }
                    .map { "${it.title} • ${it.universityName}" }
                    .distinct()
                    .take(4)
                    .toList()
            }
            .orEmpty()

        CountryDetailUiState(
            country = country,
            relatedUniversities = relatedUniversities,
            relatedPrograms = relatedPrograms,
            isLoading = false
        )
    }
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
