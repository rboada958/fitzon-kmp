package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CreatePersonalRecordRequest(
    val exerciseName: String,
    val value: String,
    val unit: String // kg, lbs, min, sec, reps
)

@Serializable
data class PersonalRecordResponse(
    val id: String,
    val athleteId: String,
    val exerciseName: String,
    val value: String,
    val unit: String,
    val achievedAt: String
)