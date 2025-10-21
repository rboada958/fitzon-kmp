package com.tepuytech.fitzon.domain.model

data class WorkoutStats(
    val completedThisWeek: Int,
    val totalWeekGoal: Int,
    val caloriesBurned: Int,
    val totalMinutes: Int
)