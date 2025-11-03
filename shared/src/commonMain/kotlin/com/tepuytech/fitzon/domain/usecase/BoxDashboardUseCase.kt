package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.box.BoxApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResult
import com.tepuytech.fitzon.domain.repository.BoxRepository

class BoxDashboardUseCase(
    private val boxRepository: BoxRepository,
) {
    suspend operator fun invoke(): BoxDashboardResult {
        return try {
            val response = boxRepository.boxDashboard()
            BoxDashboardResult.Success(response)
        } catch (e: BoxApiException) {
            BoxDashboardResult.Error(e.errorMessage)
        }
    }
}