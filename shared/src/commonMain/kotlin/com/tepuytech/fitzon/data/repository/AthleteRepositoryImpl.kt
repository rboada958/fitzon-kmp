package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.remote.api.AthleteApi
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.repository.AthleteRepository

class AthleteRepositoryImpl(
    private val apiService: AthleteApi

) : AthleteRepository {
    override suspend fun athleteDashboard(): AthleteDashboardResponse {
        return apiService.athleteDashboard()
    }
}