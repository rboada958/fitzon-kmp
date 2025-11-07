package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CompleteWorkoutResponse(
    val id: String,
    val athleteId: String,
    val workoutId: String,
    val completedAt: String,
    val caloriesBurned: Int?,
    val durationMinutes: Int?,
    val notes: String?
)