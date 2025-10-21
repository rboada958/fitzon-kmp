package com.tepuytech.fitzon.domain.model

data class BoxStats(
    val activeMembersToday: Int,
    val totalMembers: Int,
    val newMembersThisMonth: Int,
    val pendingRenewals: Int
)