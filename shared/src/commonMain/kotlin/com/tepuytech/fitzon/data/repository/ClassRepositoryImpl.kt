package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.data.remote.api.ClassApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.EnrollmentResponse
import com.tepuytech.fitzon.domain.model.athletes.UnEnrollmentResponse
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.classes.ClassDetailsResponse
import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse
import com.tepuytech.fitzon.domain.repository.ClassRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

class ClassRepositoryImpl(
    private val apiService: ClassApi,
    private val api: TokenApi,
    private val sessionManager: SessionManager
) : ClassRepository {
    override suspend fun createClass(request: CreateClassRequest): CreateClassResponse {
        return try {
            apiService.createClass(request)
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
                    return apiService.createClass(request)
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

    override suspend fun getBoxClasses(boxId: String): List<ClassesResponse> {
        return try {
            apiService.getBoxClasses(boxId)
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
                    return apiService.getBoxClasses(boxId)
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

    override suspend fun deleteClass(classId: String) {
        try {
            apiService.deleteClass(classId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.deleteClass(classId)
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

    override suspend fun availableClasses(): AvailableClassesResponse {
       return try {
            apiService.availableClasses()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.availableClasses()
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

    override suspend fun enrollInClass(classId: String): EnrollmentResponse {
        return try {
            apiService.enrollInClass(classId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.enrollInClass(classId)
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

    override suspend fun unenrollInClass(classId: String): UnEnrollmentResponse {
        return try {
            apiService.unenrollInClass(classId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.unenrollInClass(classId)
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

    override suspend fun classDetails(classId: String): ClassDetailsResponse {
        return try {
            apiService.classDetails(classId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                try {
                    val tokenResponse = api.refreshToken()
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    apiService.classDetails(classId)
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

    override suspend fun updateClass(request: CreateClassRequest, classId: String): CreateClassResponse {
        return try {
            apiService.updateClass(request, classId)
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
                    return apiService.updateClass(request, classId)
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