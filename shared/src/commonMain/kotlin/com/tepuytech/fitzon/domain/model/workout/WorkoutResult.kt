package com.tepuytech.fitzon.domain.model.workout

sealed class WorkoutResult {
    data class Success(val workoutData: WorkoutResponse) : WorkoutResult()
    data class Error(val message: String) : WorkoutResult()
    data class Empty(val message: String) : WorkoutResult()
}