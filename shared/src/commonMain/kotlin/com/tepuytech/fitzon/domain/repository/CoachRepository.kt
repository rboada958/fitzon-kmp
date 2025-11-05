package com.tepuytech.fitzon.domain.repository

import com.tepuytech.fitzon.domain.model.coach.CoachResponse

interface CoachRepository {
    suspend fun getCoaches() : List<CoachResponse>
}