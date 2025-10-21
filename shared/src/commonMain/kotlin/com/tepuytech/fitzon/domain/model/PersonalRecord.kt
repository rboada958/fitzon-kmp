package com.tepuytech.fitzon.domain.model

data class PersonalRecord(
    val exercise: String,
    val value: String,
    val date: String,
    val isNew: Boolean = false
)