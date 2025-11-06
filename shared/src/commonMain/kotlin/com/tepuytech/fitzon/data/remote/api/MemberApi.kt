package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import com.tepuytech.fitzon.domain.model.member.MemberResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode

class MemberApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {
    suspend fun getMembers() : List<MemberResponse> {
        val response = httpClient.get("/api/members") {
            header("Authorization", "Bearer ${sessionManager.getTokenSync()}")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw ClientRequestException(response, "Unauthorized")
        }

        return response.body()
    }
}