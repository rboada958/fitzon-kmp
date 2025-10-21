package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CoachResponse(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val phone: String?,
    val specialties: List<String>,
    val certifications: List<String>,
    val status: String, // ACTIVE, ON_LEAVE, INACTIVE
    val joinedAt: String,
    val totalClasses: Int,
    val rating: Float,
    val currentClasses: Int,
    val yearsExperience: String
)

@Serializable
data class PromoteToCoachRequest(
    val athleteId: String,
    val specialties: List<String>,
    val certifications: List<String>,
    val yearsExperience: Int
)

@Serializable
data class UpdateCoachStatusRequest(
    val status: String // ACTIVE, ON_LEAVE, INACTIVE
)