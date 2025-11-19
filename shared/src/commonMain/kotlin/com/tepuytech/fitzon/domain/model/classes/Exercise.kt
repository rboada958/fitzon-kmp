package com.tepuytech.fitzon.domain.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: String? = null,
    val rest: Int? = null
)