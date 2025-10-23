package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.data.remote.api.AuthApi
import com.tepuytech.fitzon.domain.model.auth.LoginResponse
import com.tepuytech.fitzon.domain.repository.AuthRepository


class AuthRepositoryImpl(
    private val apiService: AuthApi
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): LoginResponse {
        return apiService.loginWithEmail(email, password)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return true
    }

    override suspend fun logout(): Boolean {
        return true
    }
}