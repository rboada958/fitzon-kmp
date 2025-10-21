package com.tepuytech.fitzon.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private lateinit var appContext: Context

fun initializeContext(context: Context) {
    appContext = context
}

actual fun initKoin() {
    startKoin {
        androidContext(appContext)
        modules(appModules)
    }
}