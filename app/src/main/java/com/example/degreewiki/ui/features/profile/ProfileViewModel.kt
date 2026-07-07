package com.example.degreewiki.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.AuthRepository
import com.example.degreewiki.data.repository.ProfileRepository
import com.example.degreewiki.domain.model.SavedItem
import com.example.degreewiki.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val savedItems: StateFlow<List<SavedItem>> = profileRepository.savedItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.update { true }
            _error.update { null }
            try {
                val profile = profileRepository.fetchProfile()
                _userProfile.update { profile }
                profileRepository.refreshSavedItems()
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to load profile data" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun removeSavedItem(id: String) {
        viewModelScope.launch {
            try {
                profileRepository.removeSavedItem(id)
            } catch (e: Exception) {
                _error.update { e.message ?: "Failed to remove item" }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
