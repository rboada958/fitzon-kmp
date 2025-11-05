package com.tepuytech.fitzon.domain.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val id: String,
    val time: String,
    val name: String,
    val coachName: String,
    val currentEnrollment: Long,
    val maxCapacity: Long,
    val description: String,
    val level: String,
    val dayOfWeek: String
)