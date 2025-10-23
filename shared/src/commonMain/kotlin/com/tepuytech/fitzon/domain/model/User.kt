package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val role: String? = null, // ATHLETE, BOX_OWNER, COACH
    val profileImageUrl: String? = null,
    val createdAt: String? = null
)