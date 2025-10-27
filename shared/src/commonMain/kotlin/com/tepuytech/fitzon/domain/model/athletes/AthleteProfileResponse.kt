package com.tepuytech.fitzon.domain.model.athletes

import com.tepuytech.fitzon.domain.model.Achievement
import com.tepuytech.fitzon.domain.model.Stat
import kotlinx.serialization.Serializable

@Serializable
data class AthleteProfileResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val boxName: String? = null,
    val memberSince: String? = null,
    val totalWorkouts: Int? = null,
    val currentStreak: Int? = null,
    val personalRecords: Int? = null,
    val weight: String? = null,
    val height: String? = null,
    val age: Int? = null,
    val achievements: List<Achievement>? = null,
    val stats: List<Stat>? = null,
    val message: String? = null
)