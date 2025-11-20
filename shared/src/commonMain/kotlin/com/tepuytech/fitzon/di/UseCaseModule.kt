package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.domain.usecase.AthleteDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.AthleteProfileUseCase
import com.tepuytech.fitzon.domain.usecase.AvailableClassesUseCase
import com.tepuytech.fitzon.domain.usecase.BoxDashboardUseCase
import com.tepuytech.fitzon.domain.usecase.BoxInfoUseCase
import com.tepuytech.fitzon.domain.usecase.BoxProfileUseCase
import com.tepuytech.fitzon.domain.usecase.BoxWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.BoxesUseCase
import com.tepuytech.fitzon.domain.usecase.ClassesDetailsUseCase
import com.tepuytech.fitzon.domain.usecase.ClassesUseCase
import com.tepuytech.fitzon.domain.usecase.CoachesUseCase
import com.tepuytech.fitzon.domain.usecase.CompleteWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.CreateClassUseCase
import com.tepuytech.fitzon.domain.usecase.CreateWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.DeleteClassUseCase
import com.tepuytech.fitzon.domain.usecase.DeleteWorkoutUseCase
import com.tepuytech.fitzon.domain.usecase.EnrollClassUseCase
import com.tepuytech.fitzon.domain.usecase.IsUserLoggedInUseCase
import com.tepuytech.fitzon.domain.usecase.LoginUseCase
import com.tepuytech.fitzon.domain.usecase.LogoutUseCase
import com.tepuytech.fitzon.domain.usecase.MembersUseCase
import com.tepuytech.fitzon.domain.usecase.PersonalRecordsUseCase
import com.tepuytech.fitzon.domain.usecase.RegisterUseCase
import com.tepuytech.fitzon.domain.usecase.UnEnrollClassUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateAthleteProfileUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateBoxProfileUseCase
import com.tepuytech.fitzon.domain.usecase.UpdateClassUseCase
import com.tepuytech.fitzon.domain.usecase.UserRoleUseCase
import com.tepuytech.fitzon.domain.usecase.WorkoutOfTheDayUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<LoginUseCase> {
        LoginUseCase(
            authRepository = get()
        )
    }
    factory<RegisterUseCase> {
        RegisterUseCase(
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
        PersonalRecordsUseCase(
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
        BoxWorkoutUseCase(
            workoutRepository = get()
        )
    }

    factory {
        DeleteWorkoutUseCase(
            workoutRepository = get()
        )
    }

    factory {
        CreateWorkoutUseCase(
            workoutRepository = get()
        )
    }

    factory {
        CompleteWorkoutUseCase(
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
        BoxesUseCase(
            boxRepository = get()
        )
    }

    factory {
        CoachesUseCase(
            coachRepository = get()
        )
    }

    factory {
        CreateClassUseCase(
            classRepository = get()
        )
    }

    factory {
        UpdateClassUseCase(
            classRepository = get()
        )
    }

    factory {
        ClassesUseCase(
            classRepository = get()
        )
    }

    factory {
        DeleteClassUseCase(
            classRepository = get()
        )
    }

    factory {
        AvailableClassesUseCase(
            classRepository = get()
        )
    }

    factory {
        EnrollClassUseCase(
            classRepository = get()
        )
    }

    factory {
        UnEnrollClassUseCase(
            classRepository = get()
        )
    }

    factory {
        ClassesDetailsUseCase(
            classRepository = get()
        )
    }

    factory {
        MembersUseCase(
            memberRepository = get()
        )
    }
}