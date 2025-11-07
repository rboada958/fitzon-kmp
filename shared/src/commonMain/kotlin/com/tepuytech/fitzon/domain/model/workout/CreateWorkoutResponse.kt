package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkoutResponse(
    val id: String,
    val boxId: String,
    val boxName: String,
    val title: String,
    val description: String,
    val date: String,
    val dayOfWeek: String,
    val duration: Long,
    val difficulty: String,
    val createdAt: String,
    val exercises: List<ExerciseResponse>,
)