package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class PersonalRecord(
    val exerciseName: String,
    val value: String,
    val achievedAt: String,
    val isNew: Boolean = false
)