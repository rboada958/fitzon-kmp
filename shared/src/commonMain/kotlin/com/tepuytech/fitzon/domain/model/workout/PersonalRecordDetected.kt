package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class PersonalRecordDetected(
    val exerciseName: String,
    val previousBest: Int?,  // null si es el primer PR
    val newBest: Int,
    val improvement: String,  // "+20 reps" o "First record!"
    val isNewRecord: Boolean
)