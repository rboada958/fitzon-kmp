package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse

interface ClassRepository {
    suspend fun createClass(request: CreateClassRequest): CreateClassResponse
    suspend fun getBoxClasses(boxId: String): List<ClassesResponse>
    suspend fun deleteClass(classId: String)
}