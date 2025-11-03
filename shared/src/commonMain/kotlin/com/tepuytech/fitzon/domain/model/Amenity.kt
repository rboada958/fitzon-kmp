package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Amenity(
    val name: String,
    val icon: String,
)