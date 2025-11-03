package com.tepuytech.fitzon.domain.model.box

import com.tepuytech.fitzon.domain.model.Stat
import kotlinx.serialization.Serializable

@Serializable
data class BoxProfileResponse(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val foundedYear: String = "",
    val totalMembers: Int = 0,
    val activeMembers: Int = 0,
    val coaches: Int = 0,
    val classes: Int = 0,
    val rating: Double = 0.0,
    val description: String = "",
    val stats: List<Stat>? = null,
)