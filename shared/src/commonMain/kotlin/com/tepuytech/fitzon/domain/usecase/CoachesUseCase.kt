package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.coach.CoachResult
import com.tepuytech.fitzon.domain.repository.CoachRepository

class CoachesUseCase(
    private val coachRepository: CoachRepository
) {
    suspend operator fun invoke(): CoachResult {
        return try {
            val response = coachRepository.getCoaches()
            CoachResult.Success(response)
        } catch (e: ApiException) {
            CoachResult.Error(e.errorMessage)
        }
    }
}