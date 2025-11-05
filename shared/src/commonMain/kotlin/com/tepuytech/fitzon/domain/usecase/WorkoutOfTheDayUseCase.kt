package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository

class WorkoutOfTheDayUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(): WorkoutResult {
        return try {
            val response = workoutRepository.workoutOfTheDay()
            WorkoutResult.Success(response)
        } catch (e: ApiException) {
            WorkoutResult.Error(e.errorMessage)
        }
    }
}