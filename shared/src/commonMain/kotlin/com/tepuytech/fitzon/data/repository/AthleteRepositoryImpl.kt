package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.remote.api.AthleteApi
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResponse
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class AthleteRepositoryImpl(
    private val apiService: AthleteApi

) : AthleteRepository {
    override suspend fun athleteDashboard(): AthleteDashboardResponse {
        return apiService.athleteDashboard()
    }

    override suspend fun athleteProfile(): AthleteProfileResponse {
        return apiService.athleteProfile()
    }

    override suspend fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double
    ): UpdateAthleteProfileResponse {
        return apiService.updateAthleteProfile(age, weight, height)
    }
}