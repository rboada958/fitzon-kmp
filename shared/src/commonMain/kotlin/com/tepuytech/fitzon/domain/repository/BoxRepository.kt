package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse

interface BoxRepository {
    suspend fun boxDashboard(): BoxDashboardResponse
    suspend fun boxInfo(boxId: String): BoxInfoResponse
    suspend fun boxProfile() : BoxProfileResponse
}