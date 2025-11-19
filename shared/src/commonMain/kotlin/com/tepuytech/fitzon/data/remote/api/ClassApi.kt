package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.EnrollmentResponse
import com.tepuytech.fitzon.domain.model.athletes.UnEnrollmentResponse
import com.tepuytech.fitzon.domain.model.classes.ClassDetailsResponse
import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class ClassApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun createClass(createClassRequest: CreateClassRequest): CreateClassResponse {
        val response = httpClient.post("api/classes") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
            setBody(createClassRequest)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun getBoxClasses(boxId: String): List<ClassesResponse> {
        val response = httpClient.get("api/classes/box/${boxId}") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun deleteClass(classId: String) {
        val response = httpClient.delete("/api/classes/$classId") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        when (response.status) {
            HttpStatusCode.NoContent -> return
            HttpStatusCode.Unauthorized -> throw ClientRequestException(response, "Unauthorized")
            else -> throw ClientRequestException(response, "Error deleting class: ${response.status}")
        }
    }

    suspend fun availableClasses() : AvailableClassesResponse {
        val response = httpClient.get("api/classes/available") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun enrollInClass(classId: String) : EnrollmentResponse {
        val response = httpClient.post("api/classes/${classId}/enroll") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun unenrollInClass(classId: String) : UnEnrollmentResponse {
        val response = httpClient.delete("api/classes/${classId}/enroll") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }

    suspend fun classDetails(classId: String) : ClassDetailsResponse {
        val response = httpClient.get("api/classes/$classId") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }
}