package com.tepuytech.fitzon.domain.model.classes

import com.tepuytech.fitzon.domain.enums.ClassStatus
import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val id: String,
    val time: String,
    val type: String = "CrossFit",
    val name: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int,
    val description: String,
    val level: String,
    val dayOfWeek: String,
    val duration : String = "60 min",
    val status: ClassStatus = ClassStatus.ACTIVE
)