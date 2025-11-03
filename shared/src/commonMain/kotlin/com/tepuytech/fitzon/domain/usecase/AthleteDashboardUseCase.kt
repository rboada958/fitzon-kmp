package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.domain.model.box.BoxApiException
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class AthleteDashboardUseCase(
    private val athleteRepository: AthleteRepository,
) {
    suspend operator fun invoke(): AthleteDashboardResult {
        return try {
            val response = athleteRepository.athleteDashboard()
            AthleteDashboardResult.Success(response)
        } catch (e: BoxApiException) {
            AthleteDashboardResult.Error(e.errorMessage)
        }
    }
}