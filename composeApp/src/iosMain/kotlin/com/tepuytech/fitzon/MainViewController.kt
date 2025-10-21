package com.tepuytech.fitzon

import androidx.compose.ui.window.ComposeUIViewController
import com.tepuytech.fitzon.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}