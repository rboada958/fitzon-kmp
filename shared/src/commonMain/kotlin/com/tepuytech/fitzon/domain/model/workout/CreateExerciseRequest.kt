package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CreateExerciseRequest(
    val name: String,
    val sets: Int,
    val reps: Int,
    val rx_version: String? = null,
    val scaled_version: String? = null
)