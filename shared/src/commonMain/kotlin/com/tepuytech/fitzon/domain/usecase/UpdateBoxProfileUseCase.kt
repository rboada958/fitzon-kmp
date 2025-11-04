package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResult
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileRequest
import com.tepuytech.fitzon.domain.repository.BoxRepository

class UpdateBoxProfileUseCase(
    private val boxRepository: BoxRepository,
) {
    suspend operator fun invoke(updateBoxProfileRequest: UpdateBoxProfileRequest): BoxDashboardResult {
        return try {
            val response = boxRepository.updateBoxProfile(updateBoxProfileRequest)
            BoxDashboardResult.SuccessUpdateBoxProfile(response)
        } catch (e: ApiException) {
            BoxDashboardResult.Error(e.errorMessage)
        }
    }
}