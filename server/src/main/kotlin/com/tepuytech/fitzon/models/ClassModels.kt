package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateClassRequest(
    val name: String,
    val coachId: String,
    val description: String? = null,
    val startTime: String, // "09:00 AM"
    val endTime: String, // "10:00 AM"
    val dayOfWeek: String, // MONDAY, TUESDAY, etc
    val maxCapacity: Int,
    val level: String = "BEGINNER", // BEGINNER, INTERMEDIATE, ADVANCED
    val workoutId: String? = null
)

@Serializable
data class ClassScheduleItem(
    val id: String,
    val time: String,
    val name: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int,
    val description: String,
    val level: String,
    val dayOfWeek: String,
    val isNow: Boolean = false
)

@Serializable
data class EnrollmentResponse(
    val message: String,
    val enrollment: EnrollmentDTO,
    val classInfo: ClassEnrollmentInfo
)

@Serializable
data class EnrollmentDTO(
    val id: String,
    val classId: String,
    val athleteId: String,
    val enrolledAt: String
)

@Serializable
data class ClassEnrollmentInfo(
    val id: String,
    val name: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int
)

@Serializable
data class MyClassesResponse(
    val today: List<TodayClassDTO>,
    val thisWeek: List<UpcomingClassDTO>,
    val upcoming: List<UpcomingClassDTO>
)

@Serializable
data class TodayClassDTO(
    val classId: String,
    val className: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val workout: WorkoutPreviewDTO?,
    val status: String  // "upcoming", "in_progress", "completed"
)

@Serializable
data class WorkoutPreviewDTO(
    val id: String,
    val title: String,
    val difficulty: String,
    val duration: Int,
    val isCompleted: Boolean
)

@Serializable
data class UpcomingClassDTO(
    val classId: String,
    val className: String,
    val dayOfWeek: String,
    val date: String,  // "Mañana", "Viernes 15 Nov"
    val startTime: String,
    val coachName: String,
    val hasWorkout: Boolean
)

@Serializable
data class AvailableClassesResponse(
    val classes: List<AvailableClassDTO>
)

@Serializable
data class AvailableClassDTO(
    val id: String,
    val name: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val coachName: String,
    val maxCapacity: Int,
    val currentEnrollment: Int,
    val spotsLeft: Int,
    val isEnrolled: Boolean,
    val workoutId: String?,
    val workoutTitle: String?,
    val level: String
)

@Serializable
data class ClassDetailsDTO(
    val classId: String,
    val className: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val level: String,
    val maxCapacity: Int,
    val currentEnrollment: Int,
    val spotsLeft: Int,
    val coach: CoachInfoDTO,
    val workout: WorkoutDetailsDTO?,
    val enrolledAthletes: List<EnrolledAthleteDTO>,
    val isEnrolled: Boolean  // Si el usuario actual está inscrito
)

@Serializable
data class WorkoutDetailsDTO(
    val id: String,
    val title: String,
    val description: String?,
    val duration: Int,
    val difficulty: String,
    val exercises: List<ExerciseDTO>
)

@Serializable
data class ExerciseDTO(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String?
)

@Serializable
data class EnrolledAthleteDTO(
    val id: String,
    val name: String,
    val hasCompletedWorkout: Boolean = false
)