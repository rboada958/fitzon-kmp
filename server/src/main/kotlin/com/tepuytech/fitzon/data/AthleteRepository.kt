package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.AchievementDTO
import com.tepuytech.fitzon.models.AthleteDTO
import com.tepuytech.fitzon.models.AthleteDashboardResponse
import com.tepuytech.fitzon.models.AthleteProfileResponse
import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassEnrollments
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.Coaches
import com.tepuytech.fitzon.models.LeaderboardEntryDTO
import com.tepuytech.fitzon.models.PersonalRecordDTO
import com.tepuytech.fitzon.models.PersonalRecords
import com.tepuytech.fitzon.models.StatItem
import com.tepuytech.fitzon.models.TodayClassDTO
import com.tepuytech.fitzon.models.UpcomingClassDTO
import com.tepuytech.fitzon.models.Users
import com.tepuytech.fitzon.models.WorkoutLogs
import com.tepuytech.fitzon.models.WorkoutPreviewDTO
import com.tepuytech.fitzon.models.WorkoutStatsDTO
import com.tepuytech.fitzon.models.Workouts
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

class AthleteRepository {

    fun getAllAthletes(): List<AthleteDTO> = transaction {
        try {
            Athletes
                .innerJoin(Users)
                .selectAll()
                .map { row ->
                    AthleteDTO(
                        id = row[Athletes.id].toString(),
                        userId = row[Athletes.userId].toString(),
                        name = row[Users.name] ?: "Atleta",
                        email = row[Users.email],
                        age = row[Athletes.age],
                        weight = row[Athletes.weight],
                        height = row[Athletes.height],
                        bio = row[Athletes.bio],
                        boxId = row[Athletes.boxId]?.toString(),
                        profileImageUrl = row[Users.profileImageUrl],
                        joinedAt = row[Athletes.joinedAt].toString()
                    )
                }
        } catch (e: Exception) {
            println("Error in getAllAthletes: ${e.message}")
            emptyList()
        }
    }

    fun getOrCreateProfile(userId: String): AthleteDTO? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            var athlete = Athletes.selectAll()
                .where { Athletes.userId eq uuid }
                .singleOrNull()

            if (athlete == null) {
                Athletes.insert {
                    it[Athletes.userId] = uuid
                }
            }

            athlete = Athletes.selectAll()
                .where { Athletes.userId eq uuid }
                .singleOrNull()

            athlete?.let {
                val user = Users.selectAll()
                    .where { Users.id eq uuid }
                    .singleOrNull()

                user?.let { u ->
                    AthleteDTO(
                        id = it[Athletes.id].toString(),
                        userId = it[Athletes.userId].toString(),
                        name = u[Users.name] ?: "",
                        email = u[Users.email],
                        age = it[Athletes.age],
                        weight = it[Athletes.weight],
                        height = it[Athletes.height],
                        bio = it[Athletes.bio],
                        boxId = it[Athletes.boxId]?.toString(),
                        profileImageUrl = u[Users.profileImageUrl],
                        joinedAt = it[Athletes.joinedAt].toString()
                    )
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    fun updateAthleteProfile(
        userId: String,
        age: Int? = null,
        weight: Double? = null,
        height: Double? = null,
        bio: String? = null
    ): AthleteDTO? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            Athletes.update({ Athletes.userId eq uuid }) {
                if (age != null) it[Athletes.age] = age
                if (weight != null) it[Athletes.weight] = weight
                if (height != null) it[Athletes.height] = height
                if (bio != null) it[Athletes.bio] = bio
            }

            getOrCreateProfile(userId)
        } catch (_: Exception) {
            null
        }
    }

    fun joinBox(userId: String, boxId: String): AthleteDTO? = transaction {
        try {
            val userUuid = UUID.fromString(userId)
            val boxUuid = UUID.fromString(boxId)

            Athletes.update({ Athletes.userId eq userUuid }) {
                it[Athletes.boxId] = boxUuid
            }

            getOrCreateProfile(userId)
        } catch (_: Exception) {
            null
        }
    }

    fun leaveBox(userId: String): AthleteDTO? = transaction {
        try {
            val userUuid = UUID.fromString(userId)

            Athletes.update({ Athletes.userId eq userUuid }) {
                it[Athletes.boxId] = null
            }

            getOrCreateProfile(userId)
        } catch (_: Exception) {
            null
        }
    }

    fun getAthleteDashboard(userId: String): AthleteDashboardResponse? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq uuid }
                .singleOrNull() ?: return@transaction null

            val user = Users.selectAll()
                .where { Users.id eq uuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            // Calcular streak de días (días consecutivos con workouts)
            val streakDays = calculateStreak(athleteId)

            // Estadísticas de workouts de esta semana
            val now = LocalDateTime.now()
            val startOfWeek = now.minusDays(now.dayOfWeek.value.toLong() - 1)

            val logsThisWeek = WorkoutLogs.selectAll()
                .where {
                    (WorkoutLogs.athleteId eq athleteId) and
                            (WorkoutLogs.completedAt greaterEq startOfWeek)
                }
                .toList()

            val completedThisWeek = logsThisWeek.size
            val totalCalories = logsThisWeek.sumOf { it[WorkoutLogs.caloriesBurned] ?: 0 }
            val totalMinutes = logsThisWeek.sumOf { it[WorkoutLogs.durationMinutes] ?: 0 }

            val workoutStats = WorkoutStatsDTO(
                completedThisWeek = completedThisWeek,
                totalWeekGoal = 7,
                caloriesBurned = totalCalories,
                totalMinutes = totalMinutes
            )

            // Personal Records (últimos 3, ordenados por fecha)
            val personalRecords = PersonalRecords.selectAll()
                .where { PersonalRecords.athleteId eq athleteId }
                .orderBy(PersonalRecords.achievedAt to SortOrder.DESC)
                .limit(3)
                .map {
                    val achievedAt = it[PersonalRecords.achievedAt]
                    val daysAgo = java.time.Duration.between(achievedAt, LocalDateTime.now()).toDays()

                    PersonalRecordDTO(
                        exerciseName = it[PersonalRecords.exerciseName],
                        value = "${it[PersonalRecords.value]} ${it[PersonalRecords.unit]}",
                        achievedAt = when {
                            daysAgo == 0L -> "Hoy"
                            daysAgo == 1L -> "Ayer"
                            daysAgo < 7 -> "Hace $daysAgo días"
                            daysAgo < 14 -> "Esta semana"
                            else -> "Hace ${daysAgo / 7} semanas"
                        },
                        isNew = daysAgo < 7
                    )
                }

            // Leaderboard (top 5 por puntos esta semana)
            val boxId = athlete[Athletes.boxId]
            val leaderboard = if (boxId != null) {
                calculateLeaderboard(boxId, athleteId)
            } else {
                emptyList()
            }

            val todayDayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).uppercase()

            val todayClasses = (ClassEnrollments innerJoin ClassSchedules)
                .innerJoin(Coaches)
                .innerJoin(Users)
                .selectAll()
                .where {
                    (ClassEnrollments.athleteId eq athleteId) and
                            (ClassSchedules.dayOfWeek eq todayDayOfWeek) and
                            (Coaches.id eq ClassSchedules.coachId) and
                            (Users.id eq Coaches.userId)
                }
                .map { row ->
                    val workoutId = row[ClassSchedules.workoutId]

                    val workout = if (workoutId != null) {
                        Workouts
                            .selectAll()
                            .where { Workouts.id eq workoutId }
                            .singleOrNull()
                            ?.let { w ->
                                val isCompleted = WorkoutLogs
                                    .selectAll()
                                    .where {
                                        (WorkoutLogs.athleteId eq athleteId) and
                                                (WorkoutLogs.workoutId eq workoutId) and
                                                (WorkoutLogs.completedAt greaterEq LocalDateTime.now().toLocalDate().atStartOfDay())
                                    }
                                    .count() > 0

                                WorkoutPreviewDTO(
                                    id = w[Workouts.id].toString(),
                                    title = w[Workouts.title],
                                    difficulty = w[Workouts.difficulty],
                                    duration = w[Workouts.duration],
                                    isCompleted = isCompleted
                                )
                            }
                    } else null

                    TodayClassDTO(
                        classId = row[ClassSchedules.id].toString(),
                        className = row[ClassSchedules.name],
                        startTime = row[ClassSchedules.startTime],
                        endTime = row[ClassSchedules.endTime],
                        coachName = row[Users.name] ?: "Coach",
                        workout = workout,
                        status = "upcoming"
                    )
                }

            val upcomingClasses = (ClassEnrollments innerJoin ClassSchedules)
                .innerJoin(Coaches)
                .innerJoin(Users)
                .selectAll()
                .where {
                    (ClassEnrollments.athleteId eq athleteId) and
                            (ClassSchedules.dayOfWeek neq todayDayOfWeek) and
                            (Coaches.id eq ClassSchedules.coachId) and
                            (Users.id eq Coaches.userId)
                }
                .limit(3)
                .map { row ->
                    val dayOfWeek = row[ClassSchedules.dayOfWeek]
                    val workoutId = row[ClassSchedules.workoutId]

                    UpcomingClassDTO(
                        classId = row[ClassSchedules.id].toString(),
                        className = row[ClassSchedules.name],
                        dayOfWeek = dayOfWeek,
                        date = formatUpcomingDate(dayOfWeek),
                        startTime = row[ClassSchedules.startTime],
                        coachName = row[Users.name] ?: "Coach",
                        hasWorkout = workoutId != null
                    )
                }

            val dayOrder = mapOf(
                "MONDAY" to 1,
                "TUESDAY" to 2,
                "WEDNESDAY" to 3,
                "THURSDAY" to 4,
                "FRIDAY" to 5,
                "SATURDAY" to 6,
                "SUNDAY" to 7
            )

            val sortedClasses = upcomingClasses.sortedWith(
                compareBy(
                    { dayOrder[it.dayOfWeek] ?: 8 },  // Order por día
                    { parseTime(it.startTime) }  // Por hora
                )
            )

            AthleteDashboardResponse(
                userName = user[Users.name] ?: "Atleta",
                streakDays = streakDays,
                workoutStats = workoutStats,
                personalRecords = personalRecords,
                leaderboard = leaderboard,
                todayClasses = todayClasses,
                hasWorkoutToday = todayClasses.any { it.workout != null },
                upcomingClasses = sortedClasses
            )
        } catch (e: Exception) {
            println("Error getting athlete dashboard: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun formatUpcomingDate(dayOfWeek: String): String {
        val today = LocalDate.now()
        val targetDay = DayOfWeek.valueOf(dayOfWeek)
        return when (val daysUntil = (targetDay.value - today.dayOfWeek.value + 7) % 7) {
            0 -> "Hoy"
            1 -> "Mañana"
            else -> {
                val targetDate = today.plusDays(daysUntil.toLong())
                "${targetDay.getDisplayName(TextStyle.FULL, Locale("es"))} ${targetDate.dayOfMonth} ${targetDate.month.getDisplayName(TextStyle.SHORT, Locale("es"))}"
            }
        }
    }

    private fun calculateStreak(athleteId: UUID): Int {
        var streak = 0
        var currentDate = LocalDate.now()

        while (true) {
            val hasWorkout = WorkoutLogs.selectAll()
                .where {
                    (WorkoutLogs.athleteId eq athleteId) and
                            (WorkoutLogs.completedAt.date() eq currentDate)
                }
                .count() > 0

            if (!hasWorkout) break

            streak++
            currentDate = currentDate.minusDays(1)
        }

        return streak
    }

    private fun calculateLeaderboard(boxId: UUID, currentAthleteId: UUID): List<LeaderboardEntryDTO> {
        val now = LocalDateTime.now()
        val startOfWeek = now.minusDays(now.dayOfWeek.value.toLong() - 1)

        val athletePoints = Athletes.selectAll()
            .where { Athletes.boxId eq boxId }
            .map { athleteRow ->
                val athleteId = athleteRow[Athletes.id]
                val userId = athleteRow[Athletes.userId]

                val points = WorkoutLogs.selectAll()
                    .where {
                        (WorkoutLogs.athleteId eq athleteId) and
                                (WorkoutLogs.completedAt greaterEq startOfWeek)
                    }
                    .sumOf { it[WorkoutLogs.caloriesBurned] ?: 0 }

                val user = Users.selectAll()
                    .where { Users.id eq userId }
                    .singleOrNull()

                Triple(athleteId, user?.get(Users.name) ?: "Atleta", points)
            }
            .sortedByDescending { it.third }
            .take(5)

        return athletePoints.mapIndexed { index, (athleteId, name, points) ->
            LeaderboardEntryDTO(
                position = index + 1,
                athleteName = name,
                points = points,
                isCurrentUser = athleteId == currentAthleteId
            )
        }
    }

    fun getFullAthleteProfile(userId: String): AthleteProfileResponse? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq uuid }
                .singleOrNull() ?: return@transaction null

            val user = Users.selectAll()
                .where { Users.id eq uuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            // Obtener nombre del box si existe
            val boxName = athlete[Athletes.boxId]?.let { boxId ->
                Boxes.selectAll()
                    .where { Boxes.id eq boxId }
                    .singleOrNull()
                    ?.get(Boxes.name)
            }

            val boxId = athlete[Athletes.boxId]?.let { boxId ->
                Boxes.selectAll()
                    .where { Boxes.id eq boxId }
                    .singleOrNull()
                    ?.get(Boxes.id)
            }

            // Calcular total de workouts
            val totalWorkouts = WorkoutLogs.selectAll()
                .where { WorkoutLogs.athleteId eq athleteId }
                .count()
                .toInt()

            // Calcular streak actual
            val currentStreak = calculateStreak(athleteId)

            // Contar personal records
            val personalRecordsCount = PersonalRecords.selectAll()
                .where { PersonalRecords.athleteId eq athleteId }
                .count()
                .toInt()

            // Obtener achievements
            val achievements = getAchievements(totalWorkouts, currentStreak, personalRecordsCount)

            // Formatear fecha de ingreso
            val joinedAt = athlete[Athletes.joinedAt]
            val memberSince = formatMemberSince(joinedAt)

            // Stats
            val stats = listOf(
                StatItem("🏋️", totalWorkouts.toString(), "Workouts"),
                StatItem("🔥", currentStreak.toString(), "Días seguidos"),
                StatItem("📈", personalRecordsCount.toString(), "Records")
            )

            AthleteProfileResponse(
                id = athlete[Athletes.id].toString(),
                name = user[Users.name] ?: "Atleta",
                email = user[Users.email],
                boxId = boxId.toString(),
                boxName = boxName,
                memberSince = memberSince,
                totalWorkouts = totalWorkouts,
                currentStreak = currentStreak,
                personalRecords = personalRecordsCount,
                weight = athlete[Athletes.weight]?.let { "$it kg" },
                height = athlete[Athletes.height]?.let { "$it m" },
                age = athlete[Athletes.age],
                achievements = achievements,
                stats = stats
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun getAchievements(totalWorkouts: Int, streak: Int, prs: Int): List<AchievementDTO> {
        return listOf(
            AchievementDTO(
                icon = "🔥",
                name = "Racha de Fuego",
                description = "7 días consecutivos",
                isUnlocked = streak >= 7
            ),
            AchievementDTO(
                icon = "💯",
                name = "Centenario",
                description = "100 workouts completados",
                isUnlocked = totalWorkouts >= 100
            ),
            AchievementDTO(
                icon = "🏆",
                name = "Primera PR",
                description = "Primer record personal",
                isUnlocked = prs >= 1
            ),
            AchievementDTO(
                icon = "⭐",
                name = "Estrella Rising",
                description = "10 PRs establecidos",
                isUnlocked = prs >= 10
            ),
            AchievementDTO(
                icon = "👑",
                name = "Rey del Box",
                description = "Primer lugar del mes",
                isUnlocked = false
            ),
            AchievementDTO(
                icon = "💪",
                name = "Fuerza Titán",
                description = "Levantar 2x tu peso",
                isUnlocked = false
            )
        )
    }

    private fun formatMemberSince(date: LocalDateTime): String {
        val months = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        return "${months[date.monthValue - 1]} ${date.year}"
    }

    private fun parseTime(time: String): Int {
        return try {
            val parts = time.split(":")
            var hours = parts[0].toInt()
            val minutes = parts[1].take(2).toInt()
            val isPM = time.contains("PM", ignoreCase = true)

            if (isPM && hours != 12) hours += 12
            if (!isPM && hours == 12) hours = 0

            hours * 60 + minutes
        } catch (e: Exception) {
            0
        }
    }
}