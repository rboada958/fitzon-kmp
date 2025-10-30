package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class BoxDashboardResponse(
    val boxName: String,
    val boxStats: BoxStatsDTO,
    val todayClasses: List<ClassScheduleItemDTO>,
    val topAthletes: List<TopAthleteDTO>
)
@Serializable
data class BoxProfileResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val foundedYear: String,
    val totalMembers: Int,
    val activeMembers: Int,
    val coaches: Int,
    val classes: Int,
    val rating: Float,
    val description: String,
    val stats: List<StatItem>
)

@Serializable
data class BoxInfoResponse(
    val id: String,
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val schedule: String,
    val rating: Float,
    val totalReviews: Int,
    val totalMembers: Int,
    val coaches: List<CoachInfoDTO>,
    val amenities: List<String>,
    val photos: List<String>
)

@Serializable
data class UpdateBoxRequest(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val schedule: String? = null,
    val amenities: List<String>? = null,
    val photos: List<String>? = null,
    val logoUrl: String? = null
)