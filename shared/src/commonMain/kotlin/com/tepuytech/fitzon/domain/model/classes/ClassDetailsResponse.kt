package com.tepuytech.fitzon.domain.model.classes

import com.tepuytech.fitzon.domain.model.CoachInfo
import com.tepuytech.fitzon.domain.model.EnrolledAthlete
import kotlinx.serialization.Serializable

@Serializable
data class ClassDetailsResponse(
    val classId: String = "class-123",
    val className: String = "CrossFit 101",
    val dayOfWeek: String = "LUNES",
    val startTime: String = "09:00 AM",
    val endTime: String = "10:00 AM",
    val level: String = "BEGINNER",
    val maxCapacity: Int = 15,
    val currentEnrollment: Int = 3,
    val spotsLeft: Int = 12,
    val coach: CoachInfo = CoachInfo(
        icon = "coach-123",
        name = "Carlos García",
        specialty = "Weightlifting",
    ),
    val workout: WorkoutInfo = WorkoutInfo(
        id = "workout-123",
        title = "CrossFit 101",
        description = "",
        duration = 15,
        difficulty = "AVANZADO",
        exercises = emptyList()
    ),
    val enrolledAthletes: List<EnrolledAthlete> = emptyList(),
    val isEnrolled: Boolean = false,
)