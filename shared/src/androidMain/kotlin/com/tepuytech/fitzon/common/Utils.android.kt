package com.tepuytech.fitzon.common

import android.annotation.SuppressLint
import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings

@SuppressLint("StaticFieldLeak")
object ApplicationContextProvider {
    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    fun getApplicationContext(): Context {
        return context
    }
}

actual fun createSettings(): ObservableSettings {
    val context = ApplicationContextProvider.getApplicationContext()
    val sharedPreferences = context.getSharedPreferences("fitzon_prefs", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}