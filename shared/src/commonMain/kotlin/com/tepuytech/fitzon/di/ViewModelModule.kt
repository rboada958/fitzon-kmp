package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.presentation.viewmodel.AthleteViewModel
import com.tepuytech.fitzon.presentation.viewmodel.BoxViewModel
import com.tepuytech.fitzon.presentation.viewmodel.LoginViewModel
import com.tepuytech.fitzon.presentation.viewmodel.WorkoutViewModel
import org.koin.dsl.module


val viewModelModule = module {
    factory<LoginViewModel> {
        LoginViewModel(
            loginUseCase = get(),
            isUserLoggedInUseCase = get(),
            userRoleUseCase = get()
        )
    }
    factory<AthleteViewModel> {
        AthleteViewModel(
            athleteDashboardUseCase = get(),
            athleteProfileUseCase = get(),
            updateAthleteProfileUseCase = get(),
            logoutUseCase = get()
        )
    }
    factory<WorkoutViewModel> {
        WorkoutViewModel(
            workoutOfTheDayUseCase = get()
        )
    }

    factory<BoxViewModel> {
        BoxViewModel(
            boxDashboardUseCase = get(),
            boxInfoUseCase = get(),
            boxProfileUseCase = get(),
            logoutUseCase = get()
        )
    }
}
