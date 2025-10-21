package com.tepuytech.fitzon.di

import org.koin.core.context.startKoin

actual fun initKoin() {
    startKoin {
        modules(appModules)
    }
}