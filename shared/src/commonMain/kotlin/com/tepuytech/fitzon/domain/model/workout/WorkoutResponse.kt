package com.tepuytech.fitzon.domain.model.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutResponse(
    val id: String? = null,
    val boxId: String? = null,
    val boxName: String? = null,
    val classId: String? = null,
    val className: String? = null,
    val title: String? = null,
    val description: String? = null,
    val exercises: List<ExerciseResponse>? = null,
    val date: String? = null,
    val duration: Int? = null,
    val difficulty: String? = null,
    val createdAt: String? = null,
    val message: String? = null
)