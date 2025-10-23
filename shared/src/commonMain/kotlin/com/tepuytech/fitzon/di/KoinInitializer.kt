package com.tepuytech.fitzon.di

expect fun initKoin()

val appModules = listOf(
    networkModule,
    dataModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)

