package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkoutRequest(
    val title: String,
    val description: String? = null,
    val date: String, // "YYYY-MM-DD"
    val duration: Int, // minutos
    val difficulty: String, // BEGINNER, INTERMEDIATE, ADVANCED, RX
    val classId: String? = null,
    val exercises: List<CreateExerciseRequest>
)

@Serializable
data class CreateExerciseRequest(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String? = null, // "120 kg", "50 lbs", "Bodyweight"
    val notes: String? = null,
    val videoUrl: String? = null
)

@Serializable
data class WorkoutResponse(
    val id: String,
    val boxId: String,
    val boxName: String,
    val classId: String? = null,
    val className: String? = null,
    val title: String,
    val description: String? = null,
    val exercises: List<ExerciseResponse>,
    val date: String,
    val duration: Int,
    val difficulty: String,
    val createdAt: String
)

@Serializable
data class ExerciseResponse(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String? = null,
    val notes: String? = null,
    val videoUrl: String? = null
)