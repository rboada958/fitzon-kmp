package com.tepuytech.fitzon.domain.model.coach

sealed class CoachResult {
    data class Success(val coaches: List<CoachResponse>) : CoachResult()
    data class Error(val message: String) : CoachResult()
    data class Empty(val message: String) : CoachResult()
}