package com.tepuytech.fitzon.domain.model

data class WorkoutOption(
    val id: String,
    val name: String,
    val type: String,
    val description: String,
    val exerciseCount: Int
)