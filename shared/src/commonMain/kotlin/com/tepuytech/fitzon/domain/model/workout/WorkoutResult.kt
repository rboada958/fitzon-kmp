package com.tepuytech.fitzon.domain.model.workout

sealed class WorkoutResult {
    object SuccessDeleteWorkout : WorkoutResult()
    data class Success(val workoutData: WorkoutResponse) : WorkoutResult()
    data class SuccessBoxWorkout(val boxWorkoutData: List<BoxWorkoutResponse>) : WorkoutResult()
    data class SuccessCreateWorkout(val createWorkoutData: CreateWorkoutResponse) : WorkoutResult()
    data class SuccessCompleteWorkout(val completeWorkoutData: CompleteWorkoutResponse) : WorkoutResult()
    data class Error(val message: String) : WorkoutResult()
    data class Empty(val message: String) : WorkoutResult()
}