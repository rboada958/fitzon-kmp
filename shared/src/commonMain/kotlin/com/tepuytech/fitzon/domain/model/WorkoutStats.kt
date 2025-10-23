package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class WorkoutStats(
    val completedThisWeek: Int,
    val totalWeekGoal: Int,
    val caloriesBurned: Int,
    val totalMinutes: Int
)