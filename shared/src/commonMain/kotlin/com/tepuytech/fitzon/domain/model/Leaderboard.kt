package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Leaderboard(
    val position: Int,
    val athleteName: String,
    val points: Int,
    val isCurrentUser: Boolean,
)