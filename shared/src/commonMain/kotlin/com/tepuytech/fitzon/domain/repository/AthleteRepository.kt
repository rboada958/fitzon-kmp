package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse

interface AthleteRepository {
    suspend fun athleteDashboard(): AthleteDashboardResponse
    suspend fun athleteProfile(): AthleteProfileResponse
    suspend fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double
    ) : UpdateAthleteProfileResponse
}