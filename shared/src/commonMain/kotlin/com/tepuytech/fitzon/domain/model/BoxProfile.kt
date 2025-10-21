package com.tepuytech.fitzon.domain.model

data class BoxProfile(
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
)
