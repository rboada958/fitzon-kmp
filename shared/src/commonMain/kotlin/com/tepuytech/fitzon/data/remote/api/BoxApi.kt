package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileRequest
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class BoxApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun boxDashboard(): BoxDashboardResponse {
        val response = httpClient.get("api/boxes/dashboard") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun boxInfo(boxId: String): BoxInfoResponse {
        val response = httpClient.get("api/boxes/$boxId/info") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun boxProfile() : BoxProfileResponse {
        val response = httpClient.get("api/boxes/profile") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun updateBoxProfile(updateBoxProfileRequest: UpdateBoxProfileRequest) : UpdateBoxProfileResponse {
        val response = httpClient.put("api/boxes/profile") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
            setBody(updateBoxProfileRequest)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }
}