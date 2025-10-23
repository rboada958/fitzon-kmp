package com.tepuytech.fitzon.domain.model.auth

import com.tepuytech.fitzon.domain.model.User
import kotlinx.serialization.Serializable
@Serializable
data class LoginResponse(
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)