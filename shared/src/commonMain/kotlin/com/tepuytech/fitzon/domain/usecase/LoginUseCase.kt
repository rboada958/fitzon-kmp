package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.model.auth.LoginResult
import com.tepuytech.fitzon.domain.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return try {
            val response = authRepository.loginWithEmail(email, password)

            if (response.user != null && response.token != null) {
                sessionManager.saveSession(
                    token = response.token,
                    userId = response.user.id,
                    email = response.user.email,
                    name = response.user.name,
                    role = response.user.role,
                    avatar = response.user.profileImageUrl
                )
                LoginResult.Success(response.user, response.token)
            } else {
                val errorMessage = response.message ?: "Unknown error"
                LoginResult.Error(errorMessage)
            }
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<LoginResponse>()
                LoginResult.Error(errorResponse.message ?: "Invalid credentials")
            } catch (_: Exception) {
                LoginResult.Error("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            LoginResult.Error("Server error")
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Connection error")
        }
    }
}