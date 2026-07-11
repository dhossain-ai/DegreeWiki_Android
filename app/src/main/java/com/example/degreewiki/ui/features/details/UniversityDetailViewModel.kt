package com.example.degreewiki.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.DataRepository
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.ui.navigation.UniversityDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.degreewiki.data.network.dto.UniversityDetailDto

data class UniversityDetailUiState(
    val university: University? = null,
    val countryName: String? = null,
    val relatedPrograms: List<String> = emptyList(),
    val isLoading: Boolean = true
    ,val detail: UniversityDetailDto? = null
)

@HiltViewModel(assistedFactory = UniversityDetailViewModel.Factory::class)
class UniversityDetailViewModel @AssistedInject constructor(
    @Assisted private val navKey: UniversityDetail,
    private val dataRepository: DataRepository
) : ViewModel() {

    val uiState: StateFlow<UniversityDetailUiState> = combine(
        dataRepository.getUniversityById(navKey.id),
        dataRepository.countries,
        dataRepository.programs
    ) { university, countries, programs ->
        val countryName = university?.countryId
            ?.takeUnless { it.isBlank() }
            ?.let { countryId -> countries.firstOrNull { it.id == countryId }?.name }

        val relatedPrograms = university?.name
            ?.let { universityName ->
                programs
                    .asSequence()
                    .filter { it.universityName.equals(universityName, ignoreCase = true) }
                    .map { it.title }
                    .distinct()
                    .take(4)
                    .toList()
            }
            .orEmpty()

        UniversityDetailUiState(
            university = university,
            countryName = countryName,
            relatedPrograms = relatedPrograms,
            isLoading = false,
            detail = university?.slug?.takeIf(String::isNotBlank)?.let { dataRepository.getUniversityDetail(it) }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UniversityDetailUiState(isLoading = true)
        )

    @AssistedFactory
    interface Factory {
        fun create(navKey: UniversityDetail): UniversityDetailViewModel
    }
}
