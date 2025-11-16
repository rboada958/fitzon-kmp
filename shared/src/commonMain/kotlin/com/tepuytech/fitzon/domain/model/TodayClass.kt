package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class TodayClass(
    val classId: String,
    val className: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val workout: Workout,
    val status: String,
)