package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.usecase.ClassesUseCase
import com.tepuytech.fitzon.domain.usecase.CreateClassUseCase
import com.tepuytech.fitzon.presentation.state.ClassUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassViewModel (
    private val createClassUseCase: CreateClassUseCase,
    private val classesUseCase: ClassesUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<ClassUiState>(ClassUiState.Idle)
    val uiState: StateFlow<ClassUiState> = _uiState

    fun createClass(request: CreateClassRequest) {
        screenModelScope.launch {
            _uiState.value = ClassUiState.Loading
            try {
                when (val result = createClassUseCase(request)) {
                    is ClassResult.SuccessCreateClass -> {
                        _uiState.value = ClassUiState.SuccessCreateClass(result.createClass)
                    }

                    is ClassResult.Error -> {
                        _uiState.value = ClassUiState.Error(result.message)
                    }
                    is ClassResult.Empty -> {
                        _uiState.value = ClassUiState.Empty(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getClasses(boxId: String) {
        screenModelScope.launch {
            _uiState.value = ClassUiState.Loading
            try {
                when (val result = classesUseCase(boxId)) {
                    is ClassResult.Success -> {
                        val sortedClasses = result.classes.sortedWith(
                            compareBy(
                                { dayOrder[it.dayOfWeek] ?: 8 },
                                { parseTime(it.time) }
                            )
                        )
                        _uiState.value = ClassUiState.Success(sortedClasses)
                    }

                    is ClassResult.Error -> {
                        _uiState.value = ClassUiState.Error(result.message)
                    }
                    is ClassResult.Empty -> {
                        _uiState.value = ClassUiState.Empty(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        private val dayOrder = mapOf(
            "MONDAY" to 1,
            "TUESDAY" to 2,
            "WEDNESDAY" to 3,
            "THURSDAY" to 4,
            "FRIDAY" to 5,
            "SATURDAY" to 6,
            "SUNDAY" to 7
        )

        private fun parseTime(time: String): Int {
            return try {
                val parts = time.split(":")
                var hour = parts[0].trim().toInt()
                val isPM = time.contains("PM", ignoreCase = true)

                if (isPM && hour != 12) {
                    hour += 12
                } else if (!isPM && hour == 12) {
                    hour = 0
                }

                val minute = parts[1].replace(Regex("[^0-9]"), "").toInt()
                
                hour * 60 + minute
            } catch (e: Exception) {
                println("Error parsing time: $time - ${e.message}")
                0
            }
        }
    }
}