package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.coach.CoachResponse

sealed class CoachUiState {
    object Idle : CoachUiState()
    object Loading : CoachUiState()
    data class Success(val coaches: List<CoachResponse>) : CoachUiState()
    data class Error(val message: String) : CoachUiState()
    data class Empty(val message: String) : CoachUiState()
}