package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.data.repository.AthleteRepositoryImpl
import com.tepuytech.fitzon.data.repository.AuthRepositoryImpl
import com.tepuytech.fitzon.domain.repository.AthleteRepository
import com.tepuytech.fitzon.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> {
        AuthRepositoryImpl(apiService = get())
    }

    single<AthleteRepository> {
        AthleteRepositoryImpl(apiService = get())
    }
}
