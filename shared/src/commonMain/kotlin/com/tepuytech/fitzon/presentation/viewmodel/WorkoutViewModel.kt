package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.usecase.WorkoutOfTheDayUseCase
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel (
    private val workoutOfTheDayUseCase: WorkoutOfTheDayUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Idle)
    val uiState: StateFlow<WorkoutUiState> = _uiState

    fun workoutOfTheDay() {
        screenModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                when (val result = workoutOfTheDayUseCase()) {
                    is WorkoutResult.Success -> {
                        _uiState.value = WorkoutUiState.Success(result.workoutData)
                    }

                    is WorkoutResult.Error -> {
                        _uiState.value = WorkoutUiState.Error(result.message)
                    }
                    is WorkoutResult.Empty -> {
                        _uiState.value = WorkoutUiState.Empty(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}