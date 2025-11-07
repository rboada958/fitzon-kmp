package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class DeleteWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String): WorkoutResult {
        return try {
            workoutRepository.deleteWorkout(workoutId)
            WorkoutResult.SuccessDeleteWorkout
        } catch (e: ApiException) {
            WorkoutResult.Error(e.errorMessage)
        }
    }
}