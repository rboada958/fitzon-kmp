package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.api.AthleteApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.domain.model.athletes.AthleteApiException
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

class AthleteRepositoryImpl(
    private val api: TokenApi,
    private val apiService: AthleteApi,
    private val sessionManager: SessionManager

) : AthleteRepository {
    override suspend fun athleteDashboard(): AthleteDashboardResponse {
        return try {
            apiService.athleteDashboard()
        } catch (e: ClientRequestException) {
            // Si es 401, try refresh el token
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    // Guardar los nuevos tokens
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    // Reintentar la petition
                    return apiService.athleteDashboard()
                } catch (_: Exception) {
                    throw AthleteApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw AthleteApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw AthleteApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw AthleteApiException("Server error")
        } catch (e: Exception) {
            throw AthleteApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun athleteProfile(): AthleteProfileResponse {
        return try {
            apiService.athleteProfile()
        } catch (e: ClientRequestException) {
            // Si es 401, try refresh el token
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    // Guardar los nuevos tokens
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    // Reintentar la petition
                    return apiService.athleteProfile()
                } catch (_: Exception) {
                    throw AthleteApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw AthleteApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw AthleteApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw AthleteApiException("Server error")
        } catch (e: Exception) {
            throw AthleteApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double
    ): UpdateAthleteProfileResponse {
        return try {
            apiService.updateAthleteProfile(age, weight, height)
        } catch (e: ClientRequestException) {
            // Si es 401, try refresh el token
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    // Guardar los nuevos tokens
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    // Reintentar la petition
                    return apiService.updateAthleteProfile(age, weight, height)
                } catch (_: Exception) {
                    throw AthleteApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw AthleteApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw AthleteApiException("Authentication failed")
                }
            }
        } catch (_: ServerResponseException) {
            throw AthleteApiException("Server error")
        } catch (e: Exception) {
            throw AthleteApiException(e.message ?: "Connection error")
        }
    }
}