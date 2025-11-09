package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.auth.LoginResult
import com.tepuytech.fitzon.domain.usecase.RegisterUseCase
import com.tepuytech.fitzon.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(customRegisterRequest: CustomRegisterRequest) {
        screenModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                when (val result = registerUseCase(customRegisterRequest)) {
                    is LoginResult.Success -> {
                        _uiState.value = AuthUiState.Success(result.user)
                    }
                    is LoginResult.Error -> {
                        _uiState.value = AuthUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

}