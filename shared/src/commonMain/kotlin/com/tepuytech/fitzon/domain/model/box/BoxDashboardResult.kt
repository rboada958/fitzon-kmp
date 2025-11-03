package com.tepuytech.fitzon.domain.model.box

sealed class BoxDashboardResult {
    data class Success(val dashboardData: BoxDashboardResponse) : BoxDashboardResult()
    data class Error(val message: String) : BoxDashboardResult()
}