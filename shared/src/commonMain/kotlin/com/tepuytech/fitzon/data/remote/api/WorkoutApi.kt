package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode

class WorkoutApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun workoutOfTheDay() : WorkoutResponse {
        val response = httpClient.get("/api/workouts/today") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun boxWorkouts(boxId: String) : List<BoxWorkoutResponse> {
        val response = httpClient.get("/api/workouts/box/${boxId}") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun deleteWorkout(workoutId: String) {
        val response = httpClient.delete("/api/workouts/$workoutId") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        when (response.status) {
            HttpStatusCode.NoContent -> return
            HttpStatusCode.Unauthorized -> throw ClientRequestException(response, "Unauthorized")
            else -> throw ClientRequestException(response, "Error deleting workout: ${response.status}")
        }
    }
}