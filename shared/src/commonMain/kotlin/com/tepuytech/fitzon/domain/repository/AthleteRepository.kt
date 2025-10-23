package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse

interface AthleteRepository {
    suspend fun athleteDashboard(): AthleteDashboardResponse
}