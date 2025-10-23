package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse

sealed class AthleteUiState {
    object Idle : AthleteUiState()
    object Loading : AthleteUiState()
    data class Success(val dashboardResponse: AthleteDashboardResponse) : AthleteUiState()
    data class Error(val message: String) : AthleteUiState()
}