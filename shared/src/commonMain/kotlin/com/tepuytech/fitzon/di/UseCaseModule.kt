package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.domain.usecase.AthleteDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.AthleteProfileUseCase
import com.tepuytech.fitzon.domain.usecase.BoxDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.BoxInfoUseCase
import com.tepuytech.fitzon.domain.usecase.BoxProfileUseCase
import com.tepuytech.fitzon.domain.usecase.CoachesUseCase
import com.tepuytech.fitzon.domain.usecase.IsUserLoggedInUseCase
import com.tepuytech.fitzon.domain.usecase.LoginUseCase
import com.tepuytech.fitzon.domain.usecase.LogoutUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateAthleteProfileUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateBoxProfileUseCase
import com.tepuytech.fitzon.domain.usecase.UserRoleUseCase
import com.tepuytech.fitzon.domain.usecase.WorkoutOfTheDayUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<LoginUseCase> {
        LoginUseCase(
            authRepository = get()
        )
    }
    factory {
        IsUserLoggedInUseCase(
            authRepository = get()
        )
    }
    factory {
        LogoutUseCase(
            authRepository = get()
        )
    }
    factory {
        UserRoleUseCase(
            authRepository = get()
        )
    }
    factory {
        AthleteDashboardUseCase(
            athleteRepository = get()
        )
    }
    factory {
        AthleteProfileUseCase(
            athleteRepository = get()
        )
    }
    factory {
        UpdateAthleteProfileUseCase(
            athleteRepository = get()
        )
    }
    factory {
        WorkoutOfTheDayUseCase(
            workoutRepository = get()
        )
    }

    factory {
        BoxDashboardUseCase(
            boxRepository = get()
        )
    }

    factory {
        BoxInfoUseCase(
            boxRepository = get()
        )
    }

    factory {
        BoxProfileUseCase(
            boxRepository = get()
        )
    }

    factory {
        UpdateBoxProfileUseCase(
            boxRepository = get()
        )
    }

    factory {
        CoachesUseCase(
            coachRepository = get()
        )
    }
}