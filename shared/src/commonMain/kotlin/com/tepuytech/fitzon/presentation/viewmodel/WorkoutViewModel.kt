package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.workout.CompleteWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.domain.model.workout.WorkoutResult
import com.tepuytech.fitzon.domain.usecase.BoxWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.CompleteWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.CreateWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.DeleteWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.WorkoutOfTheDayUseCase
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel (
    private val workoutOfTheDayUseCase: WorkoutOfTheDayUseCase,
    private val boxWorkoutUseCase: BoxWorkoutUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase
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

    fun boxWorkout(boxId: String) {
        screenModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                when (val result = boxWorkoutUseCase(boxId)) {
                    is WorkoutResult.SuccessBoxWorkout -> {
                        _uiState.value = WorkoutUiState.SuccessBoxWorkout(result.boxWorkoutData)
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

    fun deleteWorkout(workoutId: String) {
        screenModelScope.launch {
            _uiState.value = WorkoutUiState.LoadingDeleteWorkout
            try {
                when (val result = deleteWorkoutUseCase(workoutId)) {
                    is WorkoutResult.SuccessDeleteWorkout -> {
                        _uiState.value = WorkoutUiState.SuccessDeleteWorkout("Workout eliminado correctamente")
                    }

                    is WorkoutResult.Error -> {
                        _uiState.value = WorkoutUiState.Error(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Error eliminando workout")
            }
        }
    }

    fun createWorkout(workoutRequest: CreateWorkoutRequest) {
        screenModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                when (val result = createWorkoutUseCase(workoutRequest)) {
                    is WorkoutResult.SuccessCreateWorkout -> {
                        _uiState.value = WorkoutUiState.SuccessCreateWorkout(result.createWorkoutData)
                    }

                    is WorkoutResult.Error -> {
                        _uiState.value = WorkoutUiState.Error(result.message)
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Error al crear workout")
            }
        }
    }

    fun completeWorkout(id: String, calories: Int, duration: Int, notes: String) {
        screenModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                when (val result = completeWorkoutUseCase(
                    id,
                    CompleteWorkoutRequest(calories, duration, notes)
                )) {
                    is WorkoutResult.SuccessCompleteWorkout -> {
                        _uiState.value =
                            WorkoutUiState.SuccessCompleteWorkout(result.completeWorkoutData)
                    }

                    is WorkoutResult.Error -> {
                        _uiState.value = WorkoutUiState.Error(result.message)
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Error completando workout")
            }
        }
    }
}