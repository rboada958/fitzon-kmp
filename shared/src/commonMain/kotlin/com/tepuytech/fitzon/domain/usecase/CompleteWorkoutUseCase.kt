package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class CompleteWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String, request: CompleteWorkoutRequest): WorkoutResult {
        return try {
            val response = workoutRepository.completeWorkout(workoutId, request)
            WorkoutResult.SuccessCompleteWorkout(response)
        } catch (e: ApiException) {
            WorkoutResult.Error(e.errorMessage)
        }
    }
}