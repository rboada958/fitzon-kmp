package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileRequest
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class AthleteApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun athleteDashboard() : AthleteDashboardResponse {
        val response = httpClient.get("/api/athletes/dashboard") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun athleteProfile() : AthleteProfileResponse {
        val response = httpClient.get("/api/athletes/profile") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double
    ) : UpdateAthleteProfileResponse {
        val userId = sessionManager.getUserIdSync()
        val response = httpClient.put("/api/athletes/profile") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
            setBody(UpdateAthleteProfileRequest(userId, age, weight, height))
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }
}