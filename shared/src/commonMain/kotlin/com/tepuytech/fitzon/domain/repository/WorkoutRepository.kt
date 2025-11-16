package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse

interface WorkoutRepository {
    suspend fun workoutOfTheDay(workoutId : String) : WorkoutResponse
    suspend fun boxWorkout(boxId : String) : List<BoxWorkoutResponse>
    suspend fun deleteWorkout(workoutId: String)
    suspend fun createWorkout(workout: CreateWorkoutRequest) : CreateWorkoutResponse
    suspend fun completeWorkout(workoutId: String, request: CompleteWorkoutRequest) : CompleteWorkoutResponse
}