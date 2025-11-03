package com.tepuytech.fitzon.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val email: String, val password: String)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDTO
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null, // para atleta
    val role: String,
    val boxId: String? = null, // si se registra como atleta
    val boxName: String? = null, // si se registra como owner
    val location: String? = null,
    val phone: String? = null
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val profileImageUrl: String? = null
)

@Serializable
data class UserResponse(val user: UserDTO)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)