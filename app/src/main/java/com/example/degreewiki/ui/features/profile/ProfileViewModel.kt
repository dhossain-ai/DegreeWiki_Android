package com.example.degreewiki.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.AuthRepository
import com.example.degreewiki.data.repository.ProfileRepository
import com.example.degreewiki.data.repository.ProfileState
import com.example.degreewiki.data.repository.SaveProgramResult
import com.example.degreewiki.data.repository.SavedProgramsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val profileState: StateFlow<ProfileState> = profileRepository.profileState
    val savedProgramsState: StateFlow<SavedProgramsState> =
        profileRepository.savedProgramsState

    fun refresh() {
        viewModelScope.launch { profileRepository.refresh() }
    }

    fun removeSavedProgram(programId: String, onResult: (SaveProgramResult) -> Unit = {}) {
        viewModelScope.launch {
            onResult(profileRepository.removeSavedProgram(programId))
        }
    }

    fun logout() {
        viewModelScope.launch { runCatching { authRepository.logout() } }
    }
}
