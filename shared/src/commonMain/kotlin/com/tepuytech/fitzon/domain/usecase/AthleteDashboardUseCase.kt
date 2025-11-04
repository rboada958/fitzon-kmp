package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class AthleteDashboardUseCase(
    private val athleteRepository: AthleteRepository,
) {
    suspend operator fun invoke(): AthleteDashboardResult {
        return try {
            val response = athleteRepository.athleteDashboard()
            AthleteDashboardResult.Success(response)
        } catch (e: ApiException) {
            AthleteDashboardResult.Error(e.errorMessage)
        }
    }
}