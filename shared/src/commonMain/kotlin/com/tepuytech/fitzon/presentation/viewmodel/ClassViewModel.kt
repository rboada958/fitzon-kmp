package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.classes.ClassResult
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.usecase.AvailableClassesUseCase
import com.tepuytech.fitzon.domain.usecase.ClassesDetailsUseCase
import com.tepuytech.fitzon.domain.usecase.ClassesUseCase
import com.tepuytech.fitzon.domain.usecase.CreateClassUseCase
import com.tepuytech.fitzon.domain.usecase.DeleteClassUseCase
import com.tepuytech.fitzon.domain.usecase.EnrollClassUseCase
import com.tepuytech.fitzon.domain.usecase.UnEnrollClassUseCase
import com.tepuytech.fitzon.presentation.state.ClassUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassViewModel (
    private val createClassUseCase: CreateClassUseCase,
    private val classesUseCase: ClassesUseCase,
    private val deleteClassUseCase: DeleteClassUseCase,
    private val availableClassesUseCase: AvailableClassesUseCase,
    private val enrollClassUseCase: EnrollClassUseCase,
    private val unenrollClassUseCase: UnEnrollClassUseCase,
    private val classesDetailsUseCase: ClassesDetailsUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<ClassUiState>(ClassUiState.Idle)
    val uiState: StateFlow<ClassUiState> = _uiState

    private val _availableClassesState = MutableStateFlow<ClassUiState>(ClassUiState.Idle)
    val availableClassesState: StateFlow<ClassUiState> = _availableClassesState

    private val _enrollmentState = MutableStateFlow<ClassUiState>(ClassUiState.Idle)
    val enrollmentState: StateFlow<ClassUiState> = _enrollmentState

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
                        _uiState.value = ClassUiState.Success(result.classes)
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

    fun deleteClass(classId: String) {
        screenModelScope.launch {
            _uiState.value = ClassUiState.LoadingDeleteClass
            try {
                when (val result = deleteClassUseCase(classId)) {
                    is ClassResult.SuccessDeleteClass -> {
                        _uiState.value = ClassUiState.SuccessDeleteClass("Class deleted successfully")
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

    fun loadAvailableClasses() {
        screenModelScope.launch {
            _availableClassesState.value = ClassUiState.Loading
            try {
                when (val result = availableClassesUseCase()) {
                    is ClassResult.AvailableClassesLoaded -> {
                        _availableClassesState.value = ClassUiState.AvailableClassesLoaded(result.classes)
                    }

                    is ClassResult.Error -> {
                        _availableClassesState.value = ClassUiState.Error(result.message)
                    }
                    is ClassResult.Empty -> {
                        _availableClassesState.value = ClassUiState.Empty(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _availableClassesState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun enrollInClass(classId: String) {
        screenModelScope.launch {
            _enrollmentState.value = ClassUiState.EnrollmentLoading(classId)
            try {
                when (val result = enrollClassUseCase(classId)) {
                    is ClassResult.EnrollmentSuccess -> {
                        _enrollmentState.value = ClassUiState.EnrollmentSuccess(result.response)
                    }

                    is ClassResult.Error -> {
                        _enrollmentState.value = ClassUiState.Error(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _enrollmentState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun unenrollFromClass(classId: String) {
        screenModelScope.launch {
            _enrollmentState.value = ClassUiState.EnrollmentLoading(classId)
            try {
                when (val result = unenrollClassUseCase(classId)) {
                    is ClassResult.UnenrollmentSuccess -> {
                        _enrollmentState.value = ClassUiState.UnenrollmentSuccess(result.response)
                    }

                    is ClassResult.Error -> {
                        _enrollmentState.value = ClassUiState.Error(result.message)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _enrollmentState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun classDetails(classId: String) {
        screenModelScope.launch {
            _uiState.value = ClassUiState.Loading
            try {
                when (val result = classesDetailsUseCase(classId)) {
                    is ClassResult.SuccessClassDetails -> {
                        _uiState.value = ClassUiState.SuccessClassDetails(result.response)
                    }
                    is ClassResult.Error -> {
                        _uiState.value = ClassUiState.Error(result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _enrollmentState.value = ClassUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearEnrollmentState() {
        _enrollmentState.value = ClassUiState.Idle
    }
}