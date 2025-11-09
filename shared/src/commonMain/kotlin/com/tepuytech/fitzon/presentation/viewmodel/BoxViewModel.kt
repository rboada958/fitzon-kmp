package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.box.BoxDashboardResult
import com.tepuytech.fitzon.domain.model.box.UpdateBoxProfileRequest
import com.tepuytech.fitzon.domain.usecase.BoxDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.BoxInfoUseCase
import com.tepuytech.fitzon.domain.usecase.BoxProfileUseCase
import com.tepuytech.fitzon.domain.usecase.BoxesUseCase
import com.tepuytech.fitzon.domain.usecase.LogoutUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateBoxProfileUseCase
import com.tepuytech.fitzon.presentation.state.BoxUiState
import com.tepuytech.fitzon.presentation.state.LogoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BoxViewModel(
    private val boxDashboardUseCase: BoxDashboardUseCase,
    private val boxInfoUseCase: BoxInfoUseCase,
    private val boxProfileUseCase: BoxProfileUseCase,
    private val updateBoxProfileUseCase: UpdateBoxProfileUseCase,
    private val boxesUseCase: BoxesUseCase,
    private val logoutUseCase: LogoutUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<BoxUiState>(BoxUiState.Idle)
    val uiState: StateFlow<BoxUiState> = _uiState

    private val _logoutState = MutableStateFlow<LogoutUiState>(LogoutUiState.Loading)
    val logoutState: StateFlow<LogoutUiState> = _logoutState.asStateFlow()

    fun boxDashboard() {
        screenModelScope.launch {
            _uiState.value = BoxUiState.Loading
            try {
                when (val result = boxDashboardUseCase()) {
                    is BoxDashboardResult.Success -> {
                        _uiState.value = BoxUiState.Success(result.dashboardData)
                    }

                    is BoxDashboardResult.Error -> {
                        _uiState.value = BoxUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = BoxUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun boxInfo(boxId: String) {
        screenModelScope.launch {
            _uiState.value = BoxUiState.Loading
            try {
                when (val result = boxInfoUseCase(boxId)) {
                    is BoxDashboardResult.SuccessBoxInfo -> {
                        _uiState.value = BoxUiState.SuccessBoxInfo(result.boxInfo)
                    }

                    is BoxDashboardResult.Error -> {
                        _uiState.value = BoxUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = BoxUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun boxProfile() {
        screenModelScope.launch {
            _uiState.value = BoxUiState.Loading
            try {
                when (val result = boxProfileUseCase()) {
                    is BoxDashboardResult.SuccessBoxProfile -> {
                        _uiState.value = BoxUiState.SuccessBoxProfile(result.boxProfile)
                    }

                    is BoxDashboardResult.Error -> {
                        _uiState.value = BoxUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = BoxUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateBoxProfile(updateBoxProfileRequest: UpdateBoxProfileRequest) {
        screenModelScope.launch {
            _uiState.value = BoxUiState.Loading
            try {
                when (val result = updateBoxProfileUseCase(updateBoxProfileRequest)) {
                    is BoxDashboardResult.SuccessUpdateBoxProfile -> {
                        _uiState.value  = BoxUiState.SuccessUpdateBoxProfile(result.updateBoxProfile)
                    }

                    is BoxDashboardResult.Error -> {
                        _uiState.value = BoxUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = BoxUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getBoxes() {
        screenModelScope.launch {
            _uiState.value = BoxUiState.Loading
            try {
                when (val result = boxesUseCase()) {
                    is BoxDashboardResult.SuccessBoxes -> {
                        _uiState.value = BoxUiState.SuccessBoxes(result.boxes)
                    }

                    is BoxDashboardResult.Error -> {
                        _uiState.value = BoxUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = BoxUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        screenModelScope.launch {
            _logoutState.value = LogoutUiState.Loading
            try {
                val result = logoutUseCase()
                if (result) {
                    _logoutState.value = LogoutUiState.Success
                } else {
                    _logoutState.value = LogoutUiState.Error("Logout failed")
                }
            } catch (e: Exception) {
                _logoutState.value = LogoutUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}