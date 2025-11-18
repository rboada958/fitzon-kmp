package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class PersonalRecordsUseCase(
    private val athleteRepository: AthleteRepository,
) {
    suspend operator fun invoke(): AthleteDashboardResult {
        return try {
            val response = athleteRepository.personalRecords()
            AthleteDashboardResult.PersonalRecordsSuccess(response)
        } catch (e: ApiException) {
            AthleteDashboardResult.Error(e.errorMessage)
        }
    }
}