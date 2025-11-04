package com.tepuytech.fitzon.domain.model.box

import com.tepuytech.fitzon.domain.model.CoachInfo
import kotlinx.serialization.Serializable

@Serializable
data class BoxInfoResponse(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val schedule: String = "",
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val totalMembers: Int = 0,
    val coaches: List<CoachInfo>? = null,
    val amenities: List<String>? = null,
    val photos: List<String>? = null
)