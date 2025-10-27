package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResult
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class UpdateAthleteProfileUseCase(
    private val athleteRepository: AthleteRepository
) {
    suspend operator fun invoke(age: Int, weight: Double, height: Double) : UpdateAthleteProfileResult {
        return try {
            val response = athleteRepository.updateAthleteProfile(age, weight, height)
            UpdateAthleteProfileResult.Success(response)
        } catch (e: ClientRequestException) {
            try {
                val errorResponse = e.response.body<UpdateAthleteProfileResponse>()
                UpdateAthleteProfileResult.Error(errorResponse.message ?: "Unknown error")
            } catch (_: Exception) {
                UpdateAthleteProfileResult.Error("Authentication failed")
            }
        } catch (_: ServerResponseException) {
            UpdateAthleteProfileResult.Error("Server error")
        } catch (e: Exception) {
            UpdateAthleteProfileResult.Error(e.message ?: "Connection error")
        }
    }

}