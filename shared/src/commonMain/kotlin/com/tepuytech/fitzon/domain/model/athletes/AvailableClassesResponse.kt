package com.tepuytech.fitzon.domain.model.athletes

import kotlinx.serialization.Serializable

@Serializable
data class AvailableClassesResponse(
    val classes: List<AvailableClassesResponseItem>,
)