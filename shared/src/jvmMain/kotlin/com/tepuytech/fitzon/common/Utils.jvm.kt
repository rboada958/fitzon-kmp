package com.tepuytech.fitzon.common

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

actual fun createSettings(): ObservableSettings {
    val preferences = Preferences.userRoot()
    return PreferencesSettings(preferences)
}