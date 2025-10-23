package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class AthleteDashboardUseCase(
    private val athleteRepository: AthleteRepository,
) {
    suspend operator fun invoke(): AthleteDashboardResult {
        return try {
            val response = athleteRepository.athleteDashboard()
            AthleteDashboardResult.Success(response)
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<LoginResponse>()
                AthleteDashboardResult.Error(errorResponse.message ?: "Unknown error")
            } catch (_: Exception) {
                AthleteDashboardResult.Error("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            AthleteDashboardResult.Error("Server error")
        } catch (e: Exception) {
            AthleteDashboardResult.Error(e.message ?: "Connection error")
        }
    }
}