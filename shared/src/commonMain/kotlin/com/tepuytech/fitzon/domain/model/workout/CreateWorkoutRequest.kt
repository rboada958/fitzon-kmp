package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkoutRequest(
    val title: String,
    val description: String,
    val date: String,
    val dayOfWeek: String,
    val duration: Int,
    val difficulty: String,
    val exercises: List<CreateExerciseRequest>
)