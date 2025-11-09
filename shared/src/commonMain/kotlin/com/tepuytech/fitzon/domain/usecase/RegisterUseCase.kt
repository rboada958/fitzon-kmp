package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.model.auth.AuthApiException
import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.auth.LoginResult
import com.tepuytech.fitzon.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(customRegisterRequest: CustomRegisterRequest): LoginResult {
        return try {
            val response = authRepository.registerWithEmail(customRegisterRequest)
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