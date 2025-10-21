package com.tepuytech.fitzon.data.repository

import com.tepuytech.fitzon.domain.model.LoginResponse
import com.tepuytech.fitzon.domain.repository.AuthRepository


class AuthRepositoryImpl() : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): LoginResponse {
        return LoginResponse(
            success = true,
            message = "Login successful",
            token = "dummy_token"
        )
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return true
    }

    override suspend fun logout(): Boolean {
        return true
    }
}