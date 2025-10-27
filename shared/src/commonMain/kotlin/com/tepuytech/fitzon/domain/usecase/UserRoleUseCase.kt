package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.domain.repository.AuthRepository

class UserRoleUseCase (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String = authRepository.userRole()
}