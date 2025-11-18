package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResult
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class UpdateAthleteProfileUseCase(
    private val athleteRepository: AthleteRepository
) {
    suspend operator fun invoke(age: Int, weight: Double, height: Double) : UpdateAthleteProfileResult {
        return try {
            val response = athleteRepository.updateAthleteProfile(age, weight, height)
            UpdateAthleteProfileResult.Success(response)
        } catch (e: Exception) {
            UpdateAthleteProfileResult.Error(e.message ?: "Connection error")
        }
    }

}