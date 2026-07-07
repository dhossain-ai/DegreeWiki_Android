package com.example.degreewiki.ui.features.discover

sealed interface DiscoveryUiState<out T> {
    data object Loading : DiscoveryUiState<Nothing>
    data class Error(val throwable: Throwable) : DiscoveryUiState<Nothing>
    data class Success<T>(val data: List<T>) : DiscoveryUiState<T>
}
