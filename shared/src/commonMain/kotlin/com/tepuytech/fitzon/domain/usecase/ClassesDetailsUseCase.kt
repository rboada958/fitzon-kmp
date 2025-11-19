package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.repository.ClassRepository

class ClassesDetailsUseCase(
    private val classRepository: ClassRepository
) {
    suspend operator fun invoke(boxId: String): ClassResult {
        return try {
            val response = classRepository.classDetails(boxId)
            ClassResult.SuccessClassDetails(response)
        } catch (e: ApiException) {
            ClassResult.Error(e.errorMessage)
        }
    }
}