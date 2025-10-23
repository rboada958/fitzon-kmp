package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.data.remote.HttpClientProvider
import com.tepuytech.fitzon.data.remote.api.AthleteApi
import com.tepuytech.fitzon.data.remote.api.AuthApi
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
}