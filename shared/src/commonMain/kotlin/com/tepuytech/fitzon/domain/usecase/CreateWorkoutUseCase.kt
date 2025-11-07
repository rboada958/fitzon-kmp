package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class CreateWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workout: CreateWorkoutRequest): WorkoutResult {
        return try {
            val response = workoutRepository.createWorkout(workout)
            WorkoutResult.SuccessCreateWorkout(response)
        } catch (e: ApiException) {
            WorkoutResult.Error(e.errorMessage)
        }
    }
}