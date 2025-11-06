package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class BoxWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(boxId: String): WorkoutResult {
        return try {
            val response = workoutRepository.boxWorkout(boxId)
            WorkoutResult.SuccessBoxWorkout(response)
        } catch (e: ApiException) {
            WorkoutResult.Error(e.errorMessage)
        }
    }
}