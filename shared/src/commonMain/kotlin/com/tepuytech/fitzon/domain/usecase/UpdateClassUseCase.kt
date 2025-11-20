package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.repository.ClassRepository

class UpdateClassUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(request: CreateClassRequest, classId: String): ClassResult {
        return try {
            val response = classRepository.updateClass(request, classId)
            ClassResult.SuccessCreateClass(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}