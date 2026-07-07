package com.example.degreewiki.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.degreewiki.data.repository.AuthRepository
import com.example.degreewiki.data.repository.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** UI-level state for the login form. */
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** Auth state derived directly from the repository. */
    val authState: StateFlow<AuthState> = authRepository.authState

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginUiState.value = LoginUiState.Error("Email and password must not be empty.")
            return
        }
        _loginUiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                authRepository.loginWithEmail(email, password)
                _loginUiState.value = LoginUiState.Idle
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error(
                    e.message ?: "Login failed. Please try again."
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {
                // Silently handle logout errors — the session will be cleared locally
            }
        }
    }

    fun clearError() {
        _loginUiState.value = LoginUiState.Idle
    }
}
