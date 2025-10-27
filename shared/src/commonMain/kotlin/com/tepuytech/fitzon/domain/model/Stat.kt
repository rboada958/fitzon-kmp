package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Stat(
    val icon: String,
    val value: String,
    val label: String,
)