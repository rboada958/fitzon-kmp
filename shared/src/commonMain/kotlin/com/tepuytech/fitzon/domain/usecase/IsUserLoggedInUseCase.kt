package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.repository.AuthRepository

class IsUserLoggedInUseCase (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean = authRepository.isUserLoggedIn()
}