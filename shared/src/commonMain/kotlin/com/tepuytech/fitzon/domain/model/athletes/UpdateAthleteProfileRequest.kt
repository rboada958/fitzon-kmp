package com.tepuytech.fitzon.domain.model.athletes

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAthleteProfileRequest(
    val userId: String? = null,
    val age: Int,
    val weight: Double,
    val height: Double,
)