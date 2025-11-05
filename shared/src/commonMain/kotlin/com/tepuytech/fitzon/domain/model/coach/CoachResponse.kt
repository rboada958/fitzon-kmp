package com.tepuytech.fitzon.domain.model.coach

import kotlinx.serialization.Serializable

@Serializable
data class CoachResponse(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String? = "",
    val specialties: List<String> = listOf(),
    val certifications: List<String> = listOf(),
    val status: String = "",
    val joinedAt: String = "",
    val totalClasses: Int = 0,
    val rating: Double = 0.0,
    val currentClasses: Int = 0,
    val yearsExperience: String = ""
)