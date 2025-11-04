package com.tepuytech.fitzon.domain.model.box

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBoxProfileResponse(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val phone: String,
    val ownerId: String,
    val logoUrl: String,
    val createdAt: String,
)