package com.tepuytech.fitzon.domain.model.workout

data class BoxWorkoutResponse(
    val id: String,
    val boxId: String,
    val boxName: String,
    val title: String,
    val description: String,
    val exercises: List<ExerciseResponse>,
    val date: String,
    val duration: Long,
    val difficulty: String,
    val createdAt: String,
)