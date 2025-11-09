package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val totalPRsToday: Int,
    val caloriesBurned: Int,
    val durationMinutes: Int,
)