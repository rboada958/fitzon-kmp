package com.tepuytech.fitzon.domain.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class CreateClassRequest(
    val name: String,
    val coachId: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val dayOfWeek: String,
    val maxCapacity: Long,
    val level: String,
    val workoutId: String
)