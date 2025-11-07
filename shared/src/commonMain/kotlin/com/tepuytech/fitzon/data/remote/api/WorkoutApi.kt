package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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

    suspend fun createWorkout(workoutRequest: CreateWorkoutRequest) : CreateWorkoutResponse {
        val response = httpClient.post("/api/workouts") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
            setBody(workoutRequest)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun completeWorkout(workoutId: String, request : CompleteWorkoutRequest) : CompleteWorkoutResponse {
        val response = httpClient.post("/api/workouts/$workoutId/complete") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
            setBody(request)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        if (response.status.value in 200..299) {
            return response.body()
        } else {
            throw ClientRequestException(response, "Request failed: ${response.status}")
        }
    }
}