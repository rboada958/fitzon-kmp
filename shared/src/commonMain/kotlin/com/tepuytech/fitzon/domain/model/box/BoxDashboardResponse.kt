package com.tepuytech.fitzon.domain.model.box

import com.tepuytech.fitzon.domain.model.BoxStats
import com.tepuytech.fitzon.domain.model.ClassSchedule
import com.tepuytech.fitzon.domain.model.TopAthlete
import kotlinx.serialization.Serializable

@Serializable
data class BoxDashboardResponse(
    val boxId: String? = null,
    val boxName: String? = null,
    val boxStats: BoxStats? = null,
    val todayClasses: List<ClassSchedule>? = null,
    val topAthletes: List<TopAthlete>? = null,
    val message: String? = null
)