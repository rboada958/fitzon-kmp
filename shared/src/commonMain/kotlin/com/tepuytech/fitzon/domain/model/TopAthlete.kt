package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class TopAthlete(
    val name: String,
    val achievement: String,
    val icon: String
)