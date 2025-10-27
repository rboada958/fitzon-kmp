package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.repository.WorkoutRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class WorkoutOfTheDayUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(): WorkoutResult {
        return try {
            val response = workoutRepository.workoutOfTheDay()

            if (!response.message.isNullOrEmpty()) {
                WorkoutResult.Empty(response.message)
            }
            else {
                WorkoutResult.Success(response)
            }
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<LoginResponse>()
                WorkoutResult.Error(errorResponse.message ?: "Unknown error")
            } catch (_: Exception) {
                WorkoutResult.Error("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            WorkoutResult.Error("Server error")
        } catch (e: Exception) {
            WorkoutResult.Error(e.message ?: "Connection error")
        }
    }
}