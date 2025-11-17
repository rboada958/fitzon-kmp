package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class AvailableClassesUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(): ClassResult {
        return try {
            val response = classRepository.availableClasses()
            ClassResult.AvailableClassesLoaded(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}