package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResult
import com.tepuytech.fitzon.domain.usecase.AthleteDashboardUseCase
import com.tepuytech.fitzon.presentation.state.AthleteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AthleteViewModel (
    private val athleteDashboardUseCase: AthleteDashboardUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<AthleteUiState>(AthleteUiState.Idle)
    val uiState: StateFlow<AthleteUiState> = _uiState

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
                }
            } catch (e: Exception) {
                _uiState.value = AthleteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}