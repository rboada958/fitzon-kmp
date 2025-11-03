package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.api.BoxApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.domain.model.box.BoxApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse
import com.tepuytech.fitzon.domain.repository.BoxRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

class BoxRepositoryImpl(
    private val api: TokenApi,
    private val apiService: BoxApi,
    private val sessionManager: SessionManager
) : BoxRepository {
    override suspend fun boxDashboard(): BoxDashboardResponse {
        return try {
            apiService.boxDashboard()
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
                    return apiService.boxDashboard()
                } catch (_: Exception) {
                    throw BoxApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw BoxApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw BoxApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw BoxApiException("Server error")
        } catch (e: Exception) {
            throw BoxApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun boxInfo(boxId: String): BoxInfoResponse {
        return try {
            apiService.boxInfo(boxId)
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
                    return apiService.boxInfo(boxId)
                } catch (_: Exception) {
                    throw BoxApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw BoxApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw BoxApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw BoxApiException("Server error")
        } catch (e: Exception) {
            throw BoxApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun boxProfile(): BoxProfileResponse {
        return try {
            apiService.boxProfile()
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
                    return apiService.boxProfile()
                } catch (_: Exception) {
                    throw BoxApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw BoxApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw BoxApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw BoxApiException("Server error")
        } catch (e: Exception) {
            throw BoxApiException(e.message ?: "Connection error")
        }
    }
}