package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.classes.ClassesResponse
import com.tepuytech.fitzon.domain.model.classes.CreateClassResponse

sealed class ClassUiState {
    object Idle : ClassUiState()
    object Loading : ClassUiState()
    data class Success(val classes: List<ClassesResponse>) : ClassUiState()
    data class SuccessCreateClass(val classes: CreateClassResponse) : ClassUiState()
    data class Error(val message: String) : ClassUiState()
    data class Empty(val message: String) : ClassUiState()
}