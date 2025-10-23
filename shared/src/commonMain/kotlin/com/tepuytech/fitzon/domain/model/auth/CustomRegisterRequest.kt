package com.tepuytech.fitzon.domain.model.auth

import kotlinx.serialization.Serializable
@Serializable
data class CustomRegisterRequest(
    val password: String, // si se registra como owner o atleta
    val email: String, // si se registra como owner o atleta
    val role: String, // si se registra como owner o atleta
    val name: String? = null, // si se registra como atleta
    val boxId: String? = null, // si se registra como atleta
    val boxName: String? = null, // si se registra como owner box
    val location: String? = null, // si se registra como owner box
    val phone: String? = null // si se registra como owner box
)