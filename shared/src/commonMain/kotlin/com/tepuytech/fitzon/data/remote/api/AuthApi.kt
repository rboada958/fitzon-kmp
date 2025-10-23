package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.auth.LoginRequest
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(
    private val httpClient: HttpClient
) {
    suspend fun loginWithEmail(email: String, password: String): LoginResponse {
        return httpClient.post("/api/auth/login") {
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun registerWithEmail(customRegisterRequest: CustomRegisterRequest): LoginResponse {
        return httpClient.post("/api/auth/register") {
            setBody(customRegisterRequest)
        }.body()
    }
}