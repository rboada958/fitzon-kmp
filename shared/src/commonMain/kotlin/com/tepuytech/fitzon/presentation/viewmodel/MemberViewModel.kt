package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tepuytech.fitzon.domain.model.member.MemberResult
import com.tepuytech.fitzon.domain.usecase.MembersUseCase
import com.tepuytech.fitzon.presentation.state.MemberUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberViewModel (
    private val membersUseCase: MembersUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<MemberUiState>(MemberUiState.Idle)
    val uiState: StateFlow<MemberUiState> = _uiState

    fun getMembers() {
        screenModelScope.launch {
            _uiState.value = MemberUiState.Loading
            try {
                when (val result = membersUseCase()) {
                    is MemberResult.Success -> {
                        _uiState.value = MemberUiState.Success(result.members)
                    }

                    is MemberResult.Error -> {
                        _uiState.value = MemberUiState.Error(result.message)
                    }
                    is MemberResult.Empty -> {
                        _uiState.value = MemberUiState.Empty(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MemberUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}