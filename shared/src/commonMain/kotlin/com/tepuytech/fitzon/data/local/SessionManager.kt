package com.tepuytech.fitzon.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings

class SessionManager(settings: ObservableSettings) {

    @OptIn(ExperimentalSettingsApi::class)
    private val flowSettings: FlowSettings = settings.toFlowSettings()

    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val IS_LOGGED_IN_KEY = "is_logged_in"
        private const val USER_ID_KEY = "user_id"
        private const val USER_EMAIL_KEY = "user_email"
        private const val USER_NAME_KEY = "user_name"
        private const val USER_AVATAR_KEY = "user_avatar"
        private const val USER_ROLE_KEY = "user_role"
        private const val PIN_KEY = "pin_set"
        private const val IS_PIN_SET_KEY = "is_pin_set"
        private const val PREMIUM_KEY = "premium_user"
        private const val IS_PREMIUM_KEY = "is_premium"
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveSession(
        token: String,
        userId: String?,
        email: String?,
        name: String?,
        role: String?,
        avatar: String?
    ) {
        flowSettings.putString(TOKEN_KEY, token)
        flowSettings.putBoolean(IS_LOGGED_IN_KEY, true)
        userId?.let { flowSettings.putString(USER_ID_KEY, it) }
        email?.let { flowSettings.putString(USER_EMAIL_KEY, it) }
        name?.let { flowSettings.putString(USER_NAME_KEY, it) }
        role?.let { flowSettings.putString(USER_ROLE_KEY, it) }
        avatar?.let { flowSettings.putString(USER_AVATAR_KEY, it) }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getTokenSync(): String? {
        return flowSettings.getStringOrNull(TOKEN_KEY)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getUserIdSync(): String? {
        return flowSettings.getStringOrNull(USER_ID_KEY)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun isLoggedInSync(): Boolean {
        return flowSettings.getBoolean(IS_LOGGED_IN_KEY, defaultValue = false)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clearSession() {
        flowSettings.putBoolean(IS_LOGGED_IN_KEY, false)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun savePin(pin: String) {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        if (userId != null) {
            flowSettings.putBoolean(IS_PIN_SET_KEY, true)
            flowSettings.putString("$PIN_KEY-$userId", pin)
            println("PIN saved: $pin")
        } else {
            throw IllegalStateException("No user is logged in. Cannot save PIN.")
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getPinSync(): String? {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        return if (userId != null) {
            flowSettings.getStringOrNull("$PIN_KEY-$userId")
        } else {
            null
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun isPinSetSync(): Boolean {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        return if (userId != null) {
            flowSettings.getStringOrNull("$PIN_KEY-$userId") != null
        } else {
            false
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clearPin() {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        if (userId != null) {
            flowSettings.remove("$PIN_KEY-$userId")
        }
        flowSettings.putBoolean(IS_PIN_SET_KEY, false)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun isPremiumActive(): Boolean {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        return if (userId != null) {
            flowSettings.getStringOrNull("$PREMIUM_KEY-$userId") != null
        } else {
            false
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clearSubscription() {
        val userId = flowSettings.getStringOrNull(USER_ID_KEY)
        if (userId != null) {
            flowSettings.remove("$PREMIUM_KEY-$userId")
        }
        flowSettings.putBoolean(IS_PREMIUM_KEY, false)
    }
}