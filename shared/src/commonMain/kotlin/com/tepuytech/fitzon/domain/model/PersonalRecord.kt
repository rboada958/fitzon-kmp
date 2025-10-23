package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class PersonalRecord(
    val exercise: String,
    val value: String,
    val date: String,
    val isNew: Boolean = false
)