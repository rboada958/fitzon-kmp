package com.tepuytech.fitzon.domain.model.athletes

import kotlinx.serialization.Serializable

@Serializable
data class PersonalRecordsResponse(
    val id: String,
    val exerciseName: String,
    val value: String,
    val achievedAt: String,
    val workoutId: String,
    val notes: String,
    val isNew: Boolean,
)