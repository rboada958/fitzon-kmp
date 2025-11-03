package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class ClassSchedule(
    val id: Int,
    val time: String,
    val name: String,
    val coachName: String,
    val currentEnrollment: Int,
    val maxCapacity: Int,
    val isNow: Boolean = false
)