package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val phone: String?,
    val membershipType: String, // Basic, Premium
    val status: String, // ACTIVE, INACTIVE, PENDING
    val joinedAt: String,
    val lastActivity: String,
    val totalWorkouts: Int,
    val paymentStatus: String // PAID, PENDING, OVERDUE
)

@Serializable
data class UpdateMemberRequest(
    val membershipType: String? = null,
    val status: String? = null,
    val paymentStatus: String? = null
)