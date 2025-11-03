package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse

interface BoxRepository {
    suspend fun boxDashboard(): BoxDashboardResponse
}