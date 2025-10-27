package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileRequest
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class AthleteApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun athleteDashboard() : AthleteDashboardResponse {
        val token = sessionManager.getTokenSync()
        return httpClient.get("/api/athletes/dashboard") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun athleteProfile() : AthleteProfileResponse {
        val token = sessionManager.getTokenSync()
        return httpClient.get("/api/athletes/profile") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double
    ) : UpdateAthleteProfileResponse {
        val token = sessionManager.getTokenSync()
        val userId = sessionManager.getUserIdSync()
        return httpClient.put("/api/athletes/profile") {
            header("Authorization", "Bearer $token")
            setBody(UpdateAthleteProfileRequest(userId, age, weight, height))
        }.body()
    }
}