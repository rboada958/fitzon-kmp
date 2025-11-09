package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResult
import com.tepuytech.fitzon.domain.repository.BoxRepository

class BoxesUseCase(
    private val boxRepository: BoxRepository,
) {
    suspend operator fun invoke(): BoxDashboardResult {
        return try {
            val response = boxRepository.getBoxes()
            BoxDashboardResult.SuccessBoxes(response)
        } catch (e: ApiException) {
            BoxDashboardResult.Error(e.errorMessage)
        }
    }
}