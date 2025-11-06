package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.data.repository.AthleteRepositoryImpl
import com.tepuytech.fitzon.data.repository.AuthRepositoryImpl
import com.tepuytech.fitzon.data.repository.BoxRepositoryImpl
import com.tepuytech.fitzon.data.repository.ClassRepositoryImpl
import com.tepuytech.fitzon.data.repository.CoachRepositoryImpl
import com.tepuytech.fitzon.data.repository.MemberRepositoryImpl
import com.tepuytech.fitzon.data.repository.WorkoutRepositoryImpl
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import com.tepuytech.fitzon.domain.repository.AuthRepository
import com.tepuytech.fitzon.domain.repository.BoxRepository
import com.tepuytech.fitzon.domain.repository.ClassRepository
import com.tepuytech.fitzon.domain.repository.CoachRepository
import com.tepuytech.fitzon.domain.repository.MemberRepository
import com.tepuytech.fitzon.domain.repository.WorkoutRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            sessionManager = get()
        )
    }

    single<AthleteRepository> {
        AthleteRepositoryImpl(
            api = get(),
            apiService = get(),
            sessionManager = get()
        )
    }

    single<WorkoutRepository> {
        WorkoutRepositoryImpl(
            apiService = get(),
            api = get(),
            sessionManager = get()
        )
    }

    single<BoxRepository> {
        BoxRepositoryImpl(
            api = get(),
            apiService = get(),
            sessionManager = get()
        )
    }

    single<CoachRepository> {
        CoachRepositoryImpl(
            api = get(),
            apiService = get(),
            sessionManager = get()
        )
    }

    single<ClassRepository> {
        ClassRepositoryImpl(
            api = get(),
            apiService = get(),
            sessionManager = get()
        )
    }

    single<MemberRepository> {
        MemberRepositoryImpl(
            api = get(),
            apiService = get(),
            sessionManager = get()
        )
    }
}
