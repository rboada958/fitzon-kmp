package com.tepuytech.fitzon.domain.model

data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val token: String? = null,
    val error: String? = null
)

