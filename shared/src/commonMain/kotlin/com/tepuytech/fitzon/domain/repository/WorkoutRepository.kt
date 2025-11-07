package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse

interface WorkoutRepository {
    suspend fun workoutOfTheDay() : WorkoutResponse
    suspend fun boxWorkout(boxId : String) : List<BoxWorkoutResponse>
    suspend fun deleteWorkout(workoutId: String)
}