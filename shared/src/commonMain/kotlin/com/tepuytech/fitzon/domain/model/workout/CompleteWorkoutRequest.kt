package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CompleteWorkoutRequest(
    val caloriesBurned: Int,
    val durationMinutes: Int,
    val notes: String,
)