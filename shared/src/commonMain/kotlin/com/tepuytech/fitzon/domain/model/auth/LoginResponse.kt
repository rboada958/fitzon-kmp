package com.tepuytech.fitzon.domain.model.auth

import com.tepuytech.fitzon.domain.model.User
import kotlinx.serialization.Serializable
@Serializable
data class LoginResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val user: User? = null,
    val message: String? = null
)