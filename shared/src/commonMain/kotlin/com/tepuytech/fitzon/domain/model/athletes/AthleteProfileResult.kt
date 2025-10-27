package com.tepuytech.fitzon.domain.model.athletes

sealed class AthleteProfileResult {
    data class Success(val dashboardData: AthleteProfileResponse) : AthleteProfileResult()
    data class Error(val message: String) : AthleteProfileResult()
}