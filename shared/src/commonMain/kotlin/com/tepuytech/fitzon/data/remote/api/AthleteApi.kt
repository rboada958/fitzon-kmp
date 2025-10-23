package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

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
}