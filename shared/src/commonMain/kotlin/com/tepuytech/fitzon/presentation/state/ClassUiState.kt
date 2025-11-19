package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.EnrollmentResponse
import com.tepuytech.fitzon.domain.model.athletes.UnEnrollmentResponse
import com.tepuytech.fitzon.domain.model.classes.ClassDetailsResponse
import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse

sealed class ClassUiState {
    object Idle : ClassUiState()
    object Loading : ClassUiState()
    object LoadingDeleteClass : ClassUiState()
    data class Success(val classes: List<ClassesResponse>) : ClassUiState()
    data class SuccessCreateClass(val classes: CreateClassResponse) : ClassUiState()
    data class SuccessDeleteClass(val message: String) : ClassUiState()
    data class Error(val message: String) : ClassUiState()
    data class Empty(val message: String) : ClassUiState()
    data class AvailableClassesLoaded(val classes: AvailableClassesResponse) : ClassUiState()

    // Enrollment
    data class EnrollmentLoading(val classId: String) : ClassUiState()
    data class EnrollmentSuccess(val response: EnrollmentResponse) : ClassUiState()
    data class UnenrollmentSuccess(val response: UnEnrollmentResponse) : ClassUiState()
    data class SuccessClassDetails(val response: ClassDetailsResponse) : ClassUiState()
}