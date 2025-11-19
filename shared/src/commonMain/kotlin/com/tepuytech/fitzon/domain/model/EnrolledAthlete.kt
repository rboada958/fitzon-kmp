package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EnrolledAthlete(
    val id: String,
    val name: String,
)