package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.member.MemberResponse

interface MemberRepository {
    suspend fun getMembers() : List<MemberResponse>
}