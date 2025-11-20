package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CoachInfo(
    val id: String,
    val name: String,
    val specialty: String,
    val icon: String
)