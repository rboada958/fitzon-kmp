package com.tepuytech.fitzon.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.tepuytech.fitzon.domain.usecase.IsUserLoggedInUseCase
import com.tepuytech.fitzon.domain.usecase.LoginUseCase

class LoginViewModel (
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ScreenModel {

}