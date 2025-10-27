package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResult
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class AthleteProfileUseCase(
    private val athleteRepository: AthleteRepository,
) {
    suspend operator fun invoke(): AthleteProfileResult {
        return try {
            val response = athleteRepository.athleteProfile()
            AthleteProfileResult.Success(response)
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<AthleteProfileResponse>()
                AthleteProfileResult.Error(errorResponse.message ?: "Unknown error")
            } catch (_: Exception) {
                AthleteProfileResult.Error("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            AthleteProfileResult.Error("Server error")
        } catch (e: Exception) {
            AthleteProfileResult.Error(e.message ?: "Connection error")
        }
    }
}