package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.remote.api.WorkoutApi
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl(
    private val apiService: WorkoutApi
) : WorkoutRepository {
    override suspend fun workoutOfTheDay(): WorkoutResponse {
        return apiService.workoutOfTheDay()
    }
}