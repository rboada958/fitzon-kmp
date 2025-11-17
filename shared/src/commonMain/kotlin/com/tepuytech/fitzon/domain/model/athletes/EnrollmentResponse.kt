package com.tepuytech.fitzon.domain.model.athletes

import com.tepuytech.fitzon.domain.model.ClassInfo
import com.tepuytech.fitzon.domain.model.Enrollment
import kotlinx.serialization.Serializable

@Serializable
data class EnrollmentResponse(
    val message: String,
    val enrollment: Enrollment,
    val classInfo: ClassInfo,
)