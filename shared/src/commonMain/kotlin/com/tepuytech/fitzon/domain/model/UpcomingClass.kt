package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class UpcomingClass(
    val classId: String,
    val className: String,
    val dayOfWeek: String,
    val date: String,
    val startTime: String,
    val coachName: String,
    val hasWorkout: Boolean,
)