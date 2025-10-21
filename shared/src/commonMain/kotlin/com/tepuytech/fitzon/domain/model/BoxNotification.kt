package com.tepuytech.fitzon.domain.model

import com.tepuytech.fitzon.domain.enums.BoxNotificationType
import com.tepuytech.fitzon.domain.enums.NotificationPriority

data class BoxNotification(
    val id: Int,
    val title: String,
    val message: String,
    val type: BoxNotificationType,
    val icon: String,
    val timestamp: String,
    val isRead: Boolean,
    val priority: NotificationPriority,
    val actionLabel: String? = null,
    val actionIcon: String? = null,
    val metadata: String? = null
)