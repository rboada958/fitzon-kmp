package com.tepuytech.fitzon.domain.model.athletes

sealed class AthleteDashboardResult {
    data class Success(val dashboardData: AthleteDashboardResponse) : AthleteDashboardResult()
    data class Error(val message: String) : AthleteDashboardResult()
}