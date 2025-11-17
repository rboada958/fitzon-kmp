package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClassInfo(
    val id: String,
    val name: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val currentEnrollment: Long,
    val maxCapacity: Long,
)