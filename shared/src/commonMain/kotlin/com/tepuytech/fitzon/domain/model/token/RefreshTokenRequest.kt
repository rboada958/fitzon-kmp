package com.tepuytech.fitzon.domain.model.token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)