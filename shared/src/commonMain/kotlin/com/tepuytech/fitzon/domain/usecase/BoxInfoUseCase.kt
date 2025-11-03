package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.box.BoxApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResult
import com.tepuytech.fitzon.domain.repository.BoxRepository

class BoxInfoUseCase(
    private val boxRepository: BoxRepository,
) {
    suspend operator fun invoke(boxId: String): BoxDashboardResult {
        return try {
            val response = boxRepository.boxInfo(boxId)
            BoxDashboardResult.SuccessBoxInfo(response)
        } catch (e: BoxApiException) {
            BoxDashboardResult.Error(e.errorMessage)
        }
    }
}