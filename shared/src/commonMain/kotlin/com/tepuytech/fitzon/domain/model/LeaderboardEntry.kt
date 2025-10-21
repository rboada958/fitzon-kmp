package com.tepuytech.fitzon.domain.model

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val points: Int,
    val isCurrentUser: Boolean = false
)