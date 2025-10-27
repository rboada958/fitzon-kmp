package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse

interface WorkoutRepository {
    suspend fun workoutOfTheDay() : WorkoutResponse
}