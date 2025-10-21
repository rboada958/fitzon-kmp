package com.tepuytech.fitzon.domain.model

import com.tepuytech.fitzon.domain.enums.NotificationType

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val type: NotificationType,
    val icon: String,
    val timestamp: String,
    val isRead: Boolean,
    val actionLabel: String? = null,
    val actionIcon: String? = null
)