package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class EnrollClassUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(classId: String): ClassResult {
        return try {
            val response = classRepository.enrollInClass(classId)
            ClassResult.EnrollmentSuccess(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}