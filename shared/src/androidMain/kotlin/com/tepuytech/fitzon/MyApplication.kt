package com.tepuytech.fitzon

import android.app.Application
import com.tepuytech.fitzon.di.initKoin
import com.tepuytech.fitzon.di.initializeContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeContext(this)
        initKoin()
    }
}