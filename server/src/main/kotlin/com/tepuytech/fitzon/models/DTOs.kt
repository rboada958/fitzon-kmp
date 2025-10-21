package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String)

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val name: String? = null,
    val role: String? = null, // ATHLETE, BOX_OWNER, COACH
    val profileImageUrl: String? = null,
    val createdAt: String? = null
)

@Serializable
data class BoxDTO(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val phone: String,
    val email: String? = null,
    val schedule: String? = null,
    val rating: Float = 0.0f,
    val totalReviews: Int = 0,
    val amenities: List<String> = emptyList(),
    val photos: List<String> = emptyList(),
    val ownerId: String,
    val logoUrl: String? = null,
    val memberCount: Int = 0,
    val coachCount: Int = 0,
    val createdAt: String? = null
)

@Serializable
data class AthleteDTO(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val age: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val bio: String? = null,
    val boxId: String? = null,
    val profileImageUrl: String? = null,
    val joinedAt: String? = null
)

@Serializable
data class CoachDTO(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val boxId: String,
    val specialties: List<String> = emptyList(),
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val joinedAt: String? = null
)

@Serializable
data class ClassDTO(
    val id: String,
    val boxId: String,
    val name: String,
    val coachId: String,
    val coachName: String,
    val description: String? = null,
    val startTime: String,
    val endTime: String,
    val dayOfWeek: String,
    val maxCapacity: Int,
    val currentEnrollment: Int = 0,
    val level: String // BEGINNER, INTERMEDIATE, ADVANCED
)

@Serializable
data class ExerciseDTO(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String? = null,
    val notes: String? = null,
    val videoUrl: String? = null
)

@Serializable
data class WorkoutDTO(
    val id: String,
    val classId: String? = null,
    val boxId: String,
    val title: String,
    val description: String? = null,
    val exercises: List<ExerciseDTO> = emptyList(),
    val date: String,
    val duration: Int, // en minutos
    val difficulty: String,
    val createdAt: String? = null
)

@Serializable
data class NotificationDTO(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String, // CLASS_UPDATE, WORKOUT_REMINDER, ANNOUNCEMENT
    val isRead: Boolean = false,
    val createdAt: String? = null
)

@Serializable
data class WorkoutStatsDTO(
    val completedThisWeek: Int,
    val totalWeekGoal: Int,
    val caloriesBurned: Int,
    val totalMinutes: Int
)

@Serializable
data class PersonalRecordDTO(
    val exerciseName: String,
    val value: String,
    val achievedAt: String,
    val isNew: Boolean = false
)

@Serializable
data class LeaderboardEntryDTO(
    val position: Int,
    val athleteName: String,
    val points: Int,
    val isCurrentUser: Boolean = false
)

@Serializable
data class AchievementDTO(
    val icon: String,
    val name: String,
    val description: String,
    val isUnlocked: Boolean
)

@Serializable
data class StatItem(
    val icon: String,
    val value: String,
    val label: String
)

@Serializable
data class BoxStatsDTO(
    val activeMembersToday: Int,
    val totalMembers: Int,
    val newMembersThisMonth: Int,
    val pendingRenewals: Int
)

@Serializable
data class ClassScheduleItemDTO(
    val id: String,
    val time: String,
    val name: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int,
    val isNow: Boolean = false
)

@Serializable
data class TopAthleteDTO(
    val name: String,
    val achievement: String,
    val icon: String
)

@Serializable
data class CoachInfoDTO(
    val name: String,
    val specialty: String,
    val icon: String
)
