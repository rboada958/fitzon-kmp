package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class WorkoutApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun workoutOfTheDay() : WorkoutResponse {
        return httpClient.get("/api/workouts/today") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }.body()
    }
}