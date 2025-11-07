package com.tepuytech.fitzon.domain.model.classes

sealed class ClassResult {
    object SuccessDeleteClass : ClassResult()
    data class Success(val classes: List<ClassesResponse>) : ClassResult()
    data class SuccessCreateClass(val createClass: CreateClassResponse) : ClassResult()
    data class Error(val message: String) : ClassResult()
    data class Empty(val message: String) : ClassResult()
}