package com.tepuytech.fitzon.presentation.state

import com.tepuytech.fitzon.domain.model.member.MemberResponse

sealed class MemberUiState {
    object Idle : MemberUiState()
    object Loading : MemberUiState()
    data class Success(val members: List<MemberResponse>) : MemberUiState()
    data class Error(val message: String) : MemberUiState()
    data class Empty(val message: String) : MemberUiState()
}