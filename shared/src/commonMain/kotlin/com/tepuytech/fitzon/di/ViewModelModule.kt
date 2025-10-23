package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.presentation.viewmodel.AthleteViewModel
import com.tepuytech.fitzon.presentation.viewmodel.LoginViewModel
import org.koin.dsl.module


val viewModelModule = module {
    factory<LoginViewModel> {
        LoginViewModel(
            loginUseCase = get(),
            isUserLoggedInUseCase = get()
        )
    }
    factory<AthleteViewModel> {
        AthleteViewModel(
            athleteDashboardUseCase = get()
        )
    }
}
