package com.tepuytech.fitzon.domain.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class UpdateClassResponse(
    val classId: String,
    val className: String,
    val description: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val level: String,
    val maxCapacity: Int,
    val currentEnrollment: Int,
    val spotsLeft: Int,
    val coachId: String,
    val coachName: String,
    val workoutId: String,
    val workoutTitle: String,
)