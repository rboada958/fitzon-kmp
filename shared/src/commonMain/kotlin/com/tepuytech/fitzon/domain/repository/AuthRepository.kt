package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.auth.LoginResponse

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): LoginResponse
    suspend fun registerWithEmail(customRegisterRequest: CustomRegisterRequest): LoginResponse

    suspend fun isUserLoggedIn(): Boolean
    suspend fun userRole() : List<String>
    suspend fun logout(): Boolean
}