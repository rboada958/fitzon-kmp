package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse

sealed class WorkoutUiState {
    object Idle : WorkoutUiState()
    object Loading : WorkoutUiState()
    data class Success(val workoutResponse: WorkoutResponse) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
    data class Empty(val message: String) : WorkoutUiState()
}