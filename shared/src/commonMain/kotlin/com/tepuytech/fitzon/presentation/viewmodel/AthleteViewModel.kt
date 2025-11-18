package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResult
import com.tepuytech.fitzon.domain.model.athletes.UpdateAthleteProfileResult
import com.tepuytech.fitzon.domain.usecase.AthleteDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.AthleteProfileUseCase
import com.tepuytech.fitzon.domain.usecase.LogoutUseCase
import com.tepuytech.fitzon.domain.usecase.PersonalRecordsUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateAthleteProfileUseCase
import com.tepuytech.fitzon.presentation.state.AthleteUiState
import com.tepuytech.fitzon.presentation.state.LogoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AthleteViewModel (
    private val athleteDashboardUseCase: AthleteDashboardUseCase,
    private val athleteProfileUseCase: AthleteProfileUseCase,
    private val updateAthleteProfileUseCase: UpdateAthleteProfileUseCase,
    private val personalRecordsUseCase: PersonalRecordsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<AthleteUiState>(AthleteUiState.Idle)
    val uiState: StateFlow<AthleteUiState> = _uiState

    private val _logoutState = MutableStateFlow<LogoutUiState>(LogoutUiState.Loading)
    val logoutState: StateFlow<LogoutUiState> = _logoutState.asStateFlow()

    fun athleteDashboard() {
        screenModelScope.launch {
            _uiState.value = AthleteUiState.Loading
            try {
                when (val result = athleteDashboardUseCase()) {
                    is AthleteDashboardResult.Success -> {
                        _uiState.value = AthleteUiState.Success(result.dashboardData)
                    }

                    is AthleteDashboardResult.Error -> {
                        _uiState.value = AthleteUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = AthleteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun athleteProfile() {
        screenModelScope.launch {
            _uiState.value = AthleteUiState.Loading
            try {
                when (val result = athleteProfileUseCase()) {
                    is AthleteProfileResult.Success -> {
                        _uiState.value = AthleteUiState.ProfileSuccess(result.dashboardData)
                    }

                    is AthleteProfileResult.Error -> {
                        _uiState.value = AthleteUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AthleteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateAthleteProfile(
        age: Int,
        weight: Double,
        height: Double,
    ) {
        screenModelScope.launch {
            _uiState.value = AthleteUiState.Loading
            try {
                when (val result = updateAthleteProfileUseCase(age, weight, height)) {
                    is UpdateAthleteProfileResult.Success -> {
                        _uiState.value = AthleteUiState.UpdateSuccess(result.update)
                    }

                    is UpdateAthleteProfileResult.Error -> {
                        _uiState.value = AthleteUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AthleteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getPersonalRecords() {
        screenModelScope.launch {
            _uiState.value = AthleteUiState.Loading
            try {
                when (val result = personalRecordsUseCase()) {
                    is AthleteDashboardResult.PersonalRecordsSuccess -> {
                        _uiState.value = AthleteUiState.PersonalRecordsSuccess(result.data)
                    }

                    is AthleteDashboardResult.Error -> {
                        _uiState.value = AthleteUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = AthleteUiState.Error(e.message ?: "Unknown error")
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