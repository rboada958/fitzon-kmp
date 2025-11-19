package com.tepuytech.fitzon.domain.model.classes

import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.EnrollmentResponse
import com.tepuytech.fitzon.domain.model.athletes.UnEnrollmentResponse

sealed class ClassResult {
    object SuccessDeleteClass : ClassResult()
    data class Success(val classes: List<ClassesResponse>) : ClassResult()
    data class SuccessCreateClass(val createClass: CreateClassResponse) : ClassResult()
    data class Error(val message: String) : ClassResult()
    data class Empty(val message: String) : ClassResult()
    data class AvailableClassesLoaded(val classes: AvailableClassesResponse) : ClassResult()
    data class EnrollmentSuccess(val response: EnrollmentResponse) : ClassResult()
    data class UnenrollmentSuccess(val response: UnEnrollmentResponse) : ClassResult()
    data class SuccessClassDetails(val response: ClassDetailsResponse) : ClassResult()
}