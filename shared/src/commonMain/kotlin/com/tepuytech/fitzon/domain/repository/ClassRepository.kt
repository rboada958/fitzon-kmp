package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.EnrollmentResponse
import com.tepuytech.fitzon.domain.model.athletes.UnEnrollmentResponse
import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse

interface ClassRepository {
    suspend fun createClass(request: CreateClassRequest): CreateClassResponse
    suspend fun getBoxClasses(boxId: String): List<ClassesResponse>
    suspend fun deleteClass(classId: String)
    suspend fun availableClasses(): AvailableClassesResponse
    suspend fun enrollInClass(classId: String): EnrollmentResponse
    suspend fun unenrollInClass(classId: String) : UnEnrollmentResponse

}