package com.tepuytech.fitzon.domain.model

data class WorkoutOption(
    val id: Int,
    val name: String,
    val type: String,
    val description: String,
    val exerciseCount: Int
)