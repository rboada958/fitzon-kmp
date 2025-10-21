package com.tepuytech.fitzon.domain.model

data class BoxInfo(
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val schedule: String,
    val rating: Float,
    val totalReviews: Int,
    val totalMembers: Int,
    val coaches: List<CoachInfo>,
    val amenities: List<String>,
    val photos: List<String>
)