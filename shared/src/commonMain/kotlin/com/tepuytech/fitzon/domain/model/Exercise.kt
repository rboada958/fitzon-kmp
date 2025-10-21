package com.tepuytech.fitzon.domain.model

data class Exercise(
    val id: Int,
    val name: String,
    val sets: Int,
    val reps: Int? = null,
    val seconds: Int? = null,
    val icon: String,
    val isCompleted: Boolean = false
)