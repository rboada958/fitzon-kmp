package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class ClassesUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(boxId: String): ClassResult {
        return try {
            val response = classRepository.getBoxClasses(boxId)
            ClassResult.Success(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}