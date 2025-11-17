package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class UnEnrollClassUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(classId: String): ClassResult {
        return try {
            val response = classRepository.unenrollInClass(classId)
            ClassResult.UnenrollmentSuccess(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}