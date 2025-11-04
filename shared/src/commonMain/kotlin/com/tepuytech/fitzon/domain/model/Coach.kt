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
    val joinedAt: String,
    val totalClasses: Int,
    val rating: Float,
    val totalStudents: Int,
    val yearsExperience: String
)