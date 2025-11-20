package com.tepuytech.fitzon.domain.model

data class ExerciseInput(
    val id: Int,
    val name: String,
    val sets: String,
    val reps: String,
    val rxVersion: String,
    val scaledVersion: String
)