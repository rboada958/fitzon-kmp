package com.tepuytech.fitzon.domain.usecase

import com.tepuytech.fitzon.data.remote.ApiException
import com.tepuytech.fitzon.domain.model.member.MemberResult
import com.tepuytech.fitzon.domain.repository.MemberRepository

class MembersUseCase(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): MemberResult {
        return try {
            val response = memberRepository.getMembers()
            MemberResult.Success(response)
        } catch (e: ApiException) {
            MemberResult.Error(e.errorMessage)
        }
    }
}