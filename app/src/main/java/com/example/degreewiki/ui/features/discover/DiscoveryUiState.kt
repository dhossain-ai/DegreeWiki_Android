package com.example.degreewiki.ui.features.discover

sealed interface DiscoveryUiState<out T> {
    data object Loading : DiscoveryUiState<Nothing>
    data object Error : DiscoveryUiState<Nothing>
    data class Success<T>(
        val data: List<T>,
        val showRefreshWarning: Boolean = false,
        val isRefreshing: Boolean = false
    ) : DiscoveryUiState<T>
}
