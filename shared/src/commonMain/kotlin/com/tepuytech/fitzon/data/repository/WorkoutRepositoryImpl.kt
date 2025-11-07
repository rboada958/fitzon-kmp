package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.ApiErrorResponse
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.data.remote.api.WorkoutApi
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import com.tepuytech.fitzon.domain.repository.WorkoutRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class WorkoutRepositoryImpl(
    private val apiService: WorkoutApi,
    private val api: TokenApi,
    private val sessionManager: SessionManager
) : WorkoutRepository {
    override suspend fun workoutOfTheDay(): WorkoutResponse {
        return try {
            apiService.workoutOfTheDay()
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
                    return apiService.workoutOfTheDay()
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

    override suspend fun boxWorkout(boxId: String): List<BoxWorkoutResponse> {
        return try {
            apiService.boxWorkouts(boxId)
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
                    return apiService.boxWorkouts(boxId)
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

    override suspend fun deleteWorkout(workoutId: String) {
        try {
            apiService.deleteWorkout(workoutId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.deleteWorkout(workoutId)
                } catch (_: Exception) {
                    throw ApiException("Authentication failed - please login again")
                }
            } else {
                try {
                    val errorResponse = e.response.body<BoxDashboardResponse>()
                    throw ApiException(errorResponse.message ?: "Unknown error")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    throw ApiException("Failed to delete workout")
                }
            }
        } catch (_: ServerResponseException) {
            throw ApiException("Server error")
        } catch (e: Exception) {
            throw ApiException(e.message ?: "Connection error")
        }
    }

    override suspend fun createWorkout(workout: CreateWorkoutRequest): CreateWorkoutResponse {
        return try {
            apiService.createWorkout(workout)
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
                    return apiService.createWorkout(workout)
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

    override suspend fun completeWorkout(
        workoutId: String,
        request: CompleteWorkoutRequest
    ): CompleteWorkoutResponse {
        return try {
            apiService.completeWorkout(workoutId, request)
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
                    return apiService.completeWorkout(workoutId, request)
                } catch (_: Exception) {
                    throw ApiException("Authentication failed - please login again")
                }
            } else {
                val errorMsg = try {
                    val err = e.response.body<ApiErrorResponse>()
                    err.message ?: "Request failed (${e.response.status})"
                } catch (_: Exception) {
                    try {
                        val text = e.response.bodyAsText()
                        Regex("\"message\"\\s*:\\s*\"([^\"]+)\"")
                            .find(text)
                            ?.groupValues?.getOrNull(1)
                            ?: text
                    } catch (_: Exception) {
                        "Request failed (${e.response.status})"
                    }
                }
                throw ApiException(errorMsg)
            }
        } catch (_: ServerResponseException) {
            throw ApiException("Server error")
        } catch (e: Exception) {
            throw ApiException(e.message ?: "Connection error")
        }
    }
}