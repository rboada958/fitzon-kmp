package com.tepuytech.fitzon.domain.model

import com.tepuytech.fitzon.domain.enums.CoachStatus

data class Coach(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val specialties: List<String>,
    val certifications: List<String>,
    val status: CoachStatus,
    val joinDate: String,
    val classesPerWeek: Int,
    val rating: Float,
    val totalStudents: Int,
    val experience: String
)