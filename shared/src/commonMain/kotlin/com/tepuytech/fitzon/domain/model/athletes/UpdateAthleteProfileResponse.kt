package com.tepuytech.fitzon.domain.model.athletes

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAthleteProfileResponse(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val boxId: String,
    val joinedAt: String,
    val message: String? = null
)