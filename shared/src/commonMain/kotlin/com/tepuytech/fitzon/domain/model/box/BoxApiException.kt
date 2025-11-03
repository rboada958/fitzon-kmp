package com.tepuytech.fitzon.domain.model.box

data class BoxApiException(val errorMessage: String) : Exception(errorMessage)