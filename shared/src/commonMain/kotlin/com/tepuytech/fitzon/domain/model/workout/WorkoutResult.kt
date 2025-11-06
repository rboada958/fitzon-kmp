package com.tepuytech.fitzon.domain.model.workout

sealed class WorkoutResult {
    data class Success(val workoutData: WorkoutResponse) : WorkoutResult()
    data class SuccessBoxWorkout(val workoutData: List<BoxWorkoutResponse>) : WorkoutResult()
    data class Error(val message: String) : WorkoutResult()
    data class Empty(val message: String) : WorkoutResult()
}