package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Enrollment(
    val id: String,
    val classId: String,
    val athleteId: String,
    val enrolledAt: String,
)