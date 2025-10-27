package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : Boolean {
        return authRepository.logout()
    }
}