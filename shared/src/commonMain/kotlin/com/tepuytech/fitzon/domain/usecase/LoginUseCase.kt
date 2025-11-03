package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.auth.AuthApiException
import com.tepuytech.fitzon.domain.model.auth.LoginResult
import com.tepuytech.fitzon.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return try {
            val response = authRepository.loginWithEmail(email, password)
            if (response.user != null) {
                LoginResult.Success(response.user)
            } else {
                LoginResult.Error("Invalid response")
            }
        } catch (e: AuthApiException) {
            LoginResult.Error(e.errorMessage)
        }
    }
}