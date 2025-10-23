package com.tepuytech.fitzon.di

import com.tepuytech.fitzon.common.createSettings
import com.tepuytech.fitzon.data.local.SessionManager
import org.koin.dsl.module

val dataModule = module {
    single { createSettings() }
    single { SessionManager(get()) }
}