package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Achievement(
    val icon: String,
    val name: String,
    val description: String,
    val isUnlocked: Boolean
)