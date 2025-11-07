package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class DeleteClassUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(classId: String): ClassResult {
        return try {
            classRepository.deleteClass(classId)
            ClassResult.SuccessDeleteClass
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}