package com.tepuytech.fitzon.domain.model

data class ClassSchedule(
    val id: Int,
    val time: String,
    val name: String,
    val coach: String,
    val currentCapacity: Int,
    val maxCapacity: Int,
    val isNow: Boolean = false
)