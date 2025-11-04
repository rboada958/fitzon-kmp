package com.tepuytech.fitzon.domain.model.box
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBoxProfileRequest(
    val name: String,
    val description: String,
    val location: String,
    val phone: String,
    val schedule: String,
    val amenities: List<String>,
    val photos: List<String>,
    val logoUrl: String,
)