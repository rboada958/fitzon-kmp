package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse

sealed class AthleteUiState {
    object Idle : AthleteUiState()
    object Loading : AthleteUiState()
    data class Success(val dashboard: AthleteDashboardResponse) : AthleteUiState()
    data class ProfileSuccess(val profile: AthleteProfileResponse) : AthleteUiState()
    data class UpdateSuccess(val update: UpdateAthleteProfileResponse) : AthleteUiState()
    data class Error(val message: String) : AthleteUiState()
}