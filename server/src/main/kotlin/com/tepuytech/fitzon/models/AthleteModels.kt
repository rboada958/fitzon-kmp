package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class AthleteDashboardResponse(
    val userName: String,
    val streakDays: Int,
    val workoutStats: WorkoutStatsDTO,
    val personalRecords: List<PersonalRecordDTO>,
    val leaderboard: List<LeaderboardEntryDTO>,
    val todayClasses: List<TodayClassDTO>,
    val hasWorkoutToday: Boolean,
    val upcomingClasses: List<UpcomingClassDTO>
)

@Serializable
data class UpdateAthleteProfileRequest(
    val age: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val bio: String? = null
)

@Serializable
data class JoinBoxRequest(
    val boxId: String
)

@Serializable
data class AthleteProfileResponse(
    val id: String,
    val name: String,
    val email: String,
    val boxId: String? = null,
    val boxName: String?,
    val memberSince: String,
    val totalWorkouts: Int,
    val currentStreak: Int,
    val personalRecords: Int,
    val weight: String?,
    val height: String?,
    val age: Int?,
    val achievements: List<AchievementDTO>,
    val stats: List<StatItem>
)

@Serializable
data class PersonalRecordHistoryDTO(
    val id: String,
    val exerciseName: String,
    val value: String,
    val achievedAt: String,
    val workoutId: String? = null,
    val notes: String? = null,
    val isNew: Boolean = false
)