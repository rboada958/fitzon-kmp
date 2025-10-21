package com.tepuytech.fitzon.domain.model

import com.tepuytech.fitzon.domain.enums.ClassStatus

data class ClassSession(
    val id: Int,
    val name: String,
    val type: String,
    val day: String,
    val time: String,
    val duration: String,
    val coach: String,
    val capacity: Int,
    val enrolled: Int,
    val level: String,
    val description: String,
    val status: ClassStatus
)