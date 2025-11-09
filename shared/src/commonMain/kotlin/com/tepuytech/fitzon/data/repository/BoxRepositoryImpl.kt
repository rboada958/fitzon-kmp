package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.api.BoxApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.auth.AuthApiException
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse
import com.tepuytech.fitzon.domain.model.box.BoxesResponse
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileRequest
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileResponse
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

    override suspend fun updateBoxProfile(updateBoxProfileRequest: UpdateBoxProfileRequest): UpdateBoxProfileResponse {
        return try {
            apiService.updateBoxProfile(updateBoxProfileRequest)
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
                    return apiService.updateBoxProfile(updateBoxProfileRequest)
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

    override suspend fun getBoxes(): List<BoxesResponse> {
        return try {
            val response = apiService.getBoxes()
            response
        }catch (e: ClientRequestException) {
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
}