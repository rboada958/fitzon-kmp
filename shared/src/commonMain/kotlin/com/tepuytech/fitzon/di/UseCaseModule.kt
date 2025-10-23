package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.domain.usecase.AthleteDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.IsUserLoggedInUseCase
import com.tepuytech.fitzon.domain.usecase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<LoginUseCase> {
        LoginUseCase(
            authRepository = get(),
            sessionManager = get()
        )
    }
    factory {
        IsUserLoggedInUseCase(
            authRepository = get()
        )
    }
    factory {
        AthleteDashboardUseCase(
            athleteRepository = get()
        )
    }
}