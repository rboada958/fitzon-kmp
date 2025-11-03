package com.tepuytech.fitzon.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class BoxStats(
    val activeMembersToday: Int,
    val totalMembers: Int,
    val newMembersThisMonth: Int,
    val pendingRenewals: Int
)