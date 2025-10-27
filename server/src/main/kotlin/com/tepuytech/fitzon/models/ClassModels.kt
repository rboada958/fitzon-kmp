package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateClassRequest(
    val name: String,
    val coachId: String,
    val description: String? = null,
    val startTime: String, // "09:00 AM"
    val endTime: String, // "10:00 AM"
    val dayOfWeek: String, // MONDAY, TUESDAY, etc
    val maxCapacity: Int,
    val level: String = "BEGINNER" // BEGINNER, INTERMEDIATE, ADVANCED
)

@Serializable
data class ClassScheduleItem(
    val id: String,
    val time: String,
    val name: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int,
    val isNow: Boolean = false
)