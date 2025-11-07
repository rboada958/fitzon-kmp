package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse

sealed class WorkoutUiState {
    object Idle : WorkoutUiState()
    object Loading : WorkoutUiState()
    object LoadingDeleteWorkout : WorkoutUiState()
    data class Success(val workoutResponse: WorkoutResponse) : WorkoutUiState()
    data class SuccessBoxWorkout(val boxWorkoutData: List<BoxWorkoutResponse>) : WorkoutUiState()
    data class SuccessDeleteWorkout(val message: String) : WorkoutUiState()
    data class SuccessCreateWorkout(val createWorkoutResponse: CreateWorkoutResponse) : WorkoutUiState()
    data class SuccessCompleteWorkout(val completeWorkoutResponse: CompleteWorkoutResponse) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
    data class Empty(val message: String) : WorkoutUiState()
}