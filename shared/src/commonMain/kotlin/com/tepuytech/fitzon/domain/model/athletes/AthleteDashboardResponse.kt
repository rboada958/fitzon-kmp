package com.tepuytech.fitzon.domain.model.athletes

import com.tepuytech.fitzon.domain.model.Leaderboard
import com.tepuytech.fitzon.domain.model.PersonalRecord
import com.tepuytech.fitzon.domain.model.WorkoutStats
import kotlinx.serialization.Serializable
@Serializable
data class AthleteDashboardResponse(
    val userName: String? = null,
    val streakDays: Int? = null,
    val workoutStats: WorkoutStats? = null,
    val personalRecords: List<PersonalRecord>? = null,
    val leaderboard: List<Leaderboard>? = null,
    val message: String? = null
)