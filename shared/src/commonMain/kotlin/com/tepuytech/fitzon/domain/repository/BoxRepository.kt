package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse
import com.tepuytech.fitzon.domain.model.box.BoxesResponse
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileRequest
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileResponse

interface BoxRepository {
    suspend fun boxDashboard(): BoxDashboardResponse
    suspend fun boxInfo(boxId: String): BoxInfoResponse
    suspend fun boxProfile() : BoxProfileResponse
    suspend fun updateBoxProfile(updateBoxProfileRequest: UpdateBoxProfileRequest) : UpdateBoxProfileResponse
    suspend fun getBoxes() : List<BoxesResponse>
}