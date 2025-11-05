package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val membershipType: String = "",
    val status: String = "",
    val joinedAt: String = "",
    val lastActivity: String = "",
    val totalWorkouts: Int = 0,
    val paymentStatus: String = ""
)