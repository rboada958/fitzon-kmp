package com.tepuytech.fitzon.domain.model.athletes

data class AthleteApiException(val errorMessage: String) : Exception(errorMessage)