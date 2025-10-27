package com.tepuytech.fitzon.presentation.state

sealed class LogoutUiState {
    object Loading : LogoutUiState()
    data class Error(val message: String) : LogoutUiState()
    object Success : LogoutUiState()
}