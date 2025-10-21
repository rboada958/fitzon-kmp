package com.tepuytech.fitzon.domain.model

data class AthleteProfile(
    val name: String,
    val email: String,
    val boxName: String,
    val memberSince: String,
    val totalWorkouts: Int,
    val currentStreak: Int,
    val personalRecords: Int,
    val weight: String,
    val height: String,
    val age: Int
)
