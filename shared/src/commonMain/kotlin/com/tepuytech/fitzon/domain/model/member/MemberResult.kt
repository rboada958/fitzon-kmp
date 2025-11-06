package com.tepuytech.fitzon.domain.model.member

sealed class MemberResult {
    data class Success(val members: List<MemberResponse>) : MemberResult()
    data class Error(val message: String) : MemberResult()
    data class Empty(val message: String) : MemberResult()
}