package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.coach.CoachResult
import com.tepuytech.fitzon.domain.usecase.CoachesUseCase
import com.tepuytech.fitzon.presentation.state.CoachUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoachViewModel (
    private val coachesUseCase: CoachesUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<CoachUiState>(CoachUiState.Idle)
    val uiState: StateFlow<CoachUiState> = _uiState

    fun getCoaches() {
        screenModelScope.launch {
            _uiState.value = CoachUiState.Loading
            try {
                when (val result = coachesUseCase()) {
                    is CoachResult.Success -> {
                        _uiState.value = CoachUiState.Success(result.coaches)
                    }

                    is CoachResult.Error -> {
                        _uiState.value = CoachUiState.Error(result.message)
                    }
                    is CoachResult.Empty -> {
                        _uiState.value = CoachUiState.Empty(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CoachUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}