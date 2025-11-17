package com.tepuytech.fitzon.domain.model.athletes

import kotlinx.serialization.Serializable

@Serializable
data class AvailableClassesResponseItem(
    val id: String,
    val name: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val maxCapacity: Int,
    val currentEnrollment: Int,
    val spotsLeft: Int,
    val isEnrolled: Boolean,
    val workoutId: String?,
    val workoutTitle: String?,
    val level: String,
)