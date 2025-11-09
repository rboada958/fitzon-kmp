package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutLog(
    val id: String,
    val athleteId: String,
    val workoutId: String,
    val completedAt: String,
    val caloriesBurned: Long,
    val durationMinutes: Long,
    val notes: String,
)