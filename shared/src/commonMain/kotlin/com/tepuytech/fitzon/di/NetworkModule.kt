package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.data.remote.HttpClientProvider
import com.tepuytech.fitzon.data.remote.api.AthleteApi
import com.tepuytech.fitzon.data.remote.api.AuthApi
import com.tepuytech.fitzon.data.remote.api.BoxApi
import com.tepuytech.fitzon.data.remote.api.ClassApi
import com.tepuytech.fitzon.data.remote.api.CoachApi
import com.tepuytech.fitzon.data.remote.api.MemberApi
import com.tepuytech.fitzon.data.remote.api.TokenApi
import com.tepuytech.fitzon.data.remote.api.WorkoutApi
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {

    single {
        HttpClientProvider.createHttpClient()
    }

    single<AuthApi> {
        AuthApi(
            httpClient = get<HttpClient>()
        )
    }

    single<AthleteApi> {
        AthleteApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<WorkoutApi> {
        WorkoutApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<BoxApi> {
        BoxApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<TokenApi> {
        TokenApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<CoachApi> {
        CoachApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<ClassApi> {
        ClassApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }

    single<MemberApi> {
        MemberApi(
            httpClient = get<HttpClient>(),
            sessionManager = get()
        )
    }
}