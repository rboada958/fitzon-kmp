package com.tepuytech.fitzon.domain.model.athletes

sealed class UpdateAthleteProfileResult {
    data class Success(val update: UpdateAthleteProfileResponse) : UpdateAthleteProfileResult()
    data class Error(val message: String) : UpdateAthleteProfileResult()
}