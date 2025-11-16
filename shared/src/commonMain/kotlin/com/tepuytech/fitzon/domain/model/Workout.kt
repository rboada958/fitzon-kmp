package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Workout(
    val id: String,
    val title: String,
    val difficulty: String,
    val duration: Long,
    val isCompleted: Boolean,
)