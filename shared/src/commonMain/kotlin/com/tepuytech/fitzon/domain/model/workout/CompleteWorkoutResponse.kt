package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CompleteWorkoutResponse(
    val message: String,
    val workoutLog: WorkoutLog,
    val personalRecords: List<PersonalRecordDetected>,
    val stats: Stats,
)