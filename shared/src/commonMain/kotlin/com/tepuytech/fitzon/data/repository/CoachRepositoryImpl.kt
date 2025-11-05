package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.data.remote.api.CoachApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.coach.CoachResponse
import com.tepuytech.fitzon.domain.repository.CoachRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

class CoachRepositoryImpl(
    private val apiService: CoachApi,
    private val api: TokenApi,
    private val sessionManager: SessionManager
) : CoachRepository {
    override suspend fun getCoaches(): List<CoachResponse> {
        return try {
            apiService.getCoaches()
        } catch (e: ClientRequestException) {
            // Si es 401, try refresh el token
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    // Reintentar la petition
                    return apiService.getCoaches()
                } catch (_: Exception) {
                    throw ApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw ApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw ApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw ApiException("Server error")
        } catch (e: Exception) {
            throw ApiException(e.message ?: "Connection error")
        }
    }
}