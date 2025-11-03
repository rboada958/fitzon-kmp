package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.token.RefreshTokenRequest
import com.tepuytech.fitzon.domain.model.token.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TokenApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {

    suspend fun refreshToken(): TokenResponse {
        val refreshToken = sessionManager.getRefreshTokenSync()
        return httpClient.post("api/auth/refresh") {
            setBody(RefreshTokenRequest(refreshToken = refreshToken ?: ""))
        }.body()
    }
}