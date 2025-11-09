package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.api.AuthApi
import com.tepuytech.fitzon.domain.model.auth.AuthApiException
import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException


class AuthRepositoryImpl(
    private val apiService: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): LoginResponse {
        return try {
            val response = apiService.loginWithEmail(email, password)

            if (response.user == null || response.accessToken == null) {
                throw AuthApiException(response.message ?: "Unknown error")
            }

            sessionManager.saveSession(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken!!,
                userId = response.user.id,
                email = response.user.email,
                name = response.user.name,
                role = response.user.roles,
                avatar = response.user.profileImageUrl
            )
            response
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<LoginResponse>()
                throw AuthApiException(errorResponse.message ?: "Invalid credentials")
            } catch (ex: AuthApiException) {
                throw ex
            } catch (_: Exception) {
                throw AuthApiException("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            throw AuthApiException("Server error")
        } catch (e: AuthApiException) {
            throw e
        } catch (e: Exception) {
            throw AuthApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun registerWithEmail(customRegisterRequest: CustomRegisterRequest): LoginResponse {
        return try {
            val response = apiService.registerWithEmail(customRegisterRequest)

            if (response.user == null || response.accessToken == null) {
                throw AuthApiException(response.message ?: "Unknown error")
            }

            sessionManager.saveSession(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken!!,
                userId = response.user.id,
                email = response.user.email,
                name = response.user.name,
                role = response.user.roles,
                avatar = response.user.profileImageUrl
            )
            response
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<LoginResponse>()
                throw AuthApiException(errorResponse.message ?: "Invalid credentials")
            } catch (ex: AuthApiException) {
                throw ex
            } catch (_: Exception) {
                throw AuthApiException("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            throw AuthApiException("Server error")
        } catch (e: AuthApiException) {
            throw e
        } catch (e: Exception) {
            throw AuthApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedInSync()
    }

    override suspend fun userRole(): List<String> {
        return sessionManager.userRoleSync()
    }

    override suspend fun logout(): Boolean {
        sessionManager.clearSession()
        return true
    }
}