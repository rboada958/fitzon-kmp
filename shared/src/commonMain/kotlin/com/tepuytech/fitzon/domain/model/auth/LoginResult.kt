package com.tepuytech.fitzon.domain.model.auth

import com.tepuytech.fitzon.domain.model.User

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
}