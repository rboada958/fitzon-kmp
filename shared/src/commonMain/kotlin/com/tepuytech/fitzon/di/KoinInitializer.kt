package com.tepuytech.fitzon.di

expect fun initKoin()

val appModules = listOf(
    repositoryModule,
    useCaseModule,
    viewModelModule
)

