package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseResponse(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String? = null,
    val notes: String? = null,
    val videoUrl: String? = null,
    val isCompleted: Boolean = false
)