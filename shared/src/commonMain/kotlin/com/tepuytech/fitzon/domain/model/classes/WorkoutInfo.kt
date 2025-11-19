package com.tepuytech.fitzon.domain.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutInfo(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val difficulty: String,
    val exercises: List<Exercise>,
)