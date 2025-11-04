package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.box.BoxDashboardResponse
import com.tepuytech.fitzon.domain.model.box.BoxInfoResponse
import com.tepuytech.fitzon.domain.model.box.BoxProfileResponse
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileResponse

sealed class BoxUiState {
    object Idle : BoxUiState()
    object Loading : BoxUiState()
    data class Success(val dashboard: BoxDashboardResponse) : BoxUiState()
    data class SuccessBoxInfo(val boxInfo: BoxInfoResponse) : BoxUiState()
    data class SuccessBoxProfile(val boxProfile: BoxProfileResponse) : BoxUiState()
    data class SuccessUpdateBoxProfile(val updateBoxProfile: UpdateBoxProfileResponse) : BoxUiState()
    data class Error(val message: String) : BoxUiState()
}