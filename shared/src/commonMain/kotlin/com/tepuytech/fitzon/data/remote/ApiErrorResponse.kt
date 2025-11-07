package com.tepuytech.fitzon.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(val message: String? = null)