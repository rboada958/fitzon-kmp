package com.tepuytech.fitzon.domain.model

import com.tepuytech.fitzon.domain.enums.MemberStatus
import com.tepuytech.fitzon.domain.enums.PaymentStatus

data class Member(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val membershipType: String,
    val status: MemberStatus,
    val joinDate: String,
    val lastActivity: String,
    val totalWorkouts: Int,
    val paymentStatus: PaymentStatus
)