package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkoutRequest(
    val title: String,
    val description: String? = null,
    val date: String, // "YYYY-MM-DD"
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
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
    val usedByClasses: List<String> = emptyList(),
    val title: String,
    val description: String? = null,
    val date: String,
    val dayOfWeek: String,
    val duration: Int,
    val difficulty: String,
    val createdAt: String,
    val exercises: List<ExerciseResponse>
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

@Serializable
data class CompleteWorkoutRequest(
    val caloriesBurned: Int,
    val durationMinutes: Int,
    val notes: String
)

@Serializable
data class WorkoutLogResponse(
    val id: String,
    val athleteId: String,
    val workoutId: String,
    val completedAt: String,
    val caloriesBurned: Int?,
    val durationMinutes: Int?,
    val notes: String?
)

@Serializable
data class CompleteWorkoutResponse(
    val message: String,
    val workoutLog: WorkoutLogResponse,
    val personalRecords: List<PersonalRecordDetected>,
    val stats: WorkoutCompletionStats
)

@Serializable
data class PersonalRecordDetected(
    val exerciseName: String,
    val previousBest: Int?,  // null si es el primer PR
    val newBest: Int,
    val improvement: String,  // "+20 reps" o "First record!"
    val isNewRecord: Boolean
)

@Serializable
data class WorkoutCompletionStats(
    val totalPRsToday: Int,
    val caloriesBurned: Int,
    val durationMinutes: Int
)

data class ExerciseData(
    val name: String,
    val sets: Int,
    val reps: Int
)