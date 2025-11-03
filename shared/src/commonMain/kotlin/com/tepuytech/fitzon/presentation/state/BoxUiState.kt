package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse

sealed class BoxUiState {
    object Idle : BoxUiState()
    object Loading : BoxUiState()
    data class Success(val dashboard: BoxDashboardResponse) : BoxUiState()
    data class Error(val message: String) : BoxUiState()
}