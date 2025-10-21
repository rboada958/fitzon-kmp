package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.BoxDTO
import com.tepuytech.fitzon.models.BoxDashboardResponse
import com.tepuytech.fitzon.models.BoxInfoResponse
import com.tepuytech.fitzon.models.BoxProfileResponse
import com.tepuytech.fitzon.models.BoxStatsDTO
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassScheduleItemDTO
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.CoachInfoDTO
import com.tepuytech.fitzon.models.Coaches
import com.tepuytech.fitzon.models.MembershipRenewals
import com.tepuytech.fitzon.models.PersonalRecords
import com.tepuytech.fitzon.models.StatItem
import com.tepuytech.fitzon.models.TopAthleteDTO
import com.tepuytech.fitzon.models.Users
import com.tepuytech.fitzon.models.WorkoutLogs
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class BoxRepository {

    fun getAllBoxes(): List<BoxDTO> = transaction {
        Boxes.selectAll().map {
            BoxDTO(
                id = it[Boxes.id].toString(),
                name = it[Boxes.name],
                description = it[Boxes.description] ?: "",
                location = it[Boxes.location],
                phone = it[Boxes.phone],
                ownerId = it[Boxes.ownerId].toString(),
                logoUrl = it[Boxes.logoUrl],
                createdAt = it[Boxes.createdAt].toString()
            )
        }
    }

    fun getBoxById(boxId: String): BoxDTO? = transaction {
        try {
            val uuid = UUID.fromString(boxId)
            Boxes.selectAll()
                .where { Boxes.id eq uuid }
                .map {
                    BoxDTO(
                        id = it[Boxes.id].toString(),
                        name = it[Boxes.name],
                        description = it[Boxes.description] ?: "",
                        location = it[Boxes.location],
                        phone = it[Boxes.phone],
                        ownerId = it[Boxes.ownerId].toString(),
                        logoUrl = it[Boxes.logoUrl],
                        createdAt = it[Boxes.createdAt].toString()
                    )
                }
                .singleOrNull()
        } catch (_: Exception) {
            null
        }
    }

    @Suppress("DefaultLocale")
    fun getBoxDashboard(userId: String): BoxDashboardResponse? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            // Obtener el box del owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq uuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Calcular estadísticas
            val totalMembers = Athletes.selectAll()
                .where { Athletes.boxId eq boxId }
                .count()
                .toInt()

            // Miembros activos hoy (con workouts hoy)
            val today = LocalDate.now()
            val activeMembersToday = if (totalMembers > 0) {
                WorkoutLogs
                    .innerJoin(Athletes)
                    .selectAll()
                    .where {
                        (Athletes.boxId eq boxId) and
                                (WorkoutLogs.completedAt.date() eq today)
                    }
                    .withDistinct()
                    .count()
                    .toInt()
            } else {
                0
            }

            // Nuevos miembros este mes
            val startOfMonth = LocalDateTime.now().withDayOfMonth(1)
            val newMembersThisMonth = Athletes.selectAll()
                .where {
                    (Athletes.boxId eq boxId) and
                            (Athletes.joinedAt greaterEq startOfMonth)
                }
                .count()
                .toInt()

            // Renovaciones pendientes
            val pendingRenewals = if (totalMembers > 0) {
                MembershipRenewals
                    .innerJoin(Athletes)
                    .selectAll()
                    .where {
                        (Athletes.boxId eq boxId) and
                                (MembershipRenewals.status eq "PENDING")
                    }
                    .count()
                    .toInt()
            } else {
                0
            }

            val boxStats = BoxStatsDTO(
                activeMembersToday = activeMembersToday,
                totalMembers = totalMembers,
                newMembersThisMonth = newMembersThisMonth,
                pendingRenewals = pendingRenewals
            )

            // Clases de hoy
            val currentDay = LocalDateTime.now().dayOfWeek
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)
                .uppercase()

            val currentTime = LocalDateTime.now()
            val currentTimeStr = String.format("%02d:%02d", currentTime.hour, currentTime.minute)

            val todayClasses = try {
                ClassSchedules.selectAll()
                    .where {
                        (ClassSchedules.boxId eq boxId) and
                                (ClassSchedules.dayOfWeek eq currentDay) and
                                (ClassSchedules.isActive eq true)
                    }
                    .orderBy(ClassSchedules.time)
                    .map { schedule ->
                        val coachId = schedule[ClassSchedules.coachId]
                        val coach = Coaches.innerJoin(Users)
                            .selectAll()
                            .where { Coaches.id eq coachId }
                            .singleOrNull()

                        val scheduleTime = schedule[ClassSchedules.time]
                        val isNow = isClassNow(scheduleTime, currentTimeStr)

                        ClassScheduleItemDTO(
                            id = schedule[ClassSchedules.id].toString(),
                            time = scheduleTime,
                            name = schedule[ClassSchedules.name],
                            coachName = coach?.get(Users.name) ?: "Coach",
                            currentEnrollment = schedule[ClassSchedules.currentEnrollment],
                            maxCapacity = schedule[ClassSchedules.maxCapacity],
                            isNow = isNow
                        )
                    }
            } catch (_: Exception) {
                emptyList()
            }

            // Top atletas
            val topAthletes = try {
                if (totalMembers > 0) {
                    getTopAthletes(boxId)
                } else {
                    emptyList()
                }
            } catch (_: Exception) {
                emptyList()
            }

            BoxDashboardResponse(
                boxName = box[Boxes.name],
                boxStats = boxStats,
                todayClasses = todayClasses,
                topAthletes = topAthletes
            )
        } catch (e: Exception) {
            println("Error in getBoxDashboard: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun getTopAthletes(boxId: UUID): List<TopAthleteDTO> {
        val now = LocalDateTime.now()
        val startOfMonth = now.withDayOfMonth(1)

        // Atleta del mes (más workouts este mes)
        val athleteOfMonth = Athletes
            .innerJoin(Users)
            .innerJoin(WorkoutLogs)
            .selectAll()
            .where {
                (Athletes.boxId eq boxId) and
                        (WorkoutLogs.completedAt greaterEq startOfMonth)
            }
            .groupBy(Athletes.id, Users.name)
            .orderBy(WorkoutLogs.id.count() to SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.get(Users.name)

        // Mejor progreso (más PRs este mes)
        val bestProgress = Athletes
            .innerJoin(Users)
            .innerJoin(PersonalRecords)
            .selectAll()
            .where {
                (Athletes.boxId eq boxId) and
                        (PersonalRecords.achievedAt greaterEq startOfMonth)
            }
            .groupBy(Athletes.id, Users.name)
            .orderBy(PersonalRecords.id.count() to SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.get(Users.name)

        // Más consistente (mayor streak)
        val mostConsistent = Athletes
            .innerJoin(Users)
            .selectAll()
            .where { Athletes.boxId eq boxId }
            .map {
                val athleteId = it[Athletes.id]
                val name = it[Users.name] ?: "Atleta"
                val streak = calculateStreakForAthlete(athleteId)
                Pair(name, streak)
            }
            .maxByOrNull { it.second }
            ?.first

        return listOfNotNull(
            athleteOfMonth?.let { TopAthleteDTO(it, "Atleta del Mes", "🏆") },
            bestProgress?.let { TopAthleteDTO(it, "Mejor Progreso", "📈") },
            mostConsistent?.let { TopAthleteDTO(it, "Más Consistente", "🔥") }
        )
    }

    fun getBoxInfoForAthlete(boxId: String): BoxInfoResponse? = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            val box = Boxes.selectAll()
                .where { Boxes.id eq uuid }
                .singleOrNull() ?: return@transaction null

            // Obtener email del owner
            val ownerId = box[Boxes.ownerId]
            val ownerEmail = Users.selectAll()
                .where { Users.id eq ownerId }
                .singleOrNull()
                ?.get(Users.email) ?: ""

            // Contar total de miembros
            val totalMembers = Athletes.selectAll()
                .where { Athletes.boxId eq uuid }
                .count()
                .toInt()

            // Obtener coaches
            val coaches = Coaches
                .innerJoin(Users)
                .selectAll()
                .where { Coaches.boxId eq uuid }
                .map { coach ->
                    val specialtiesJson = coach[Coaches.specialties]
                    val specialties = try {
                        kotlinx.serialization.json.Json.decodeFromString<List<String>>(specialtiesJson)
                    } catch (_: Exception) {
                        listOf("Coach")
                    }

                    CoachInfoDTO(
                        name = coach[Users.name] ?: "Coach",
                        specialty = specialties.firstOrNull() ?: "CrossFit Coach",
                        icon = "👨‍🏫"
                    )
                }

            // Parsear amenities (almacenadas como JSON)
            val amenitiesJson = box[Boxes.amenities]
            val amenities = try {
                kotlinx.serialization.json.Json.decodeFromString<List<String>>(amenitiesJson)
            } catch (_: Exception) {
                listOf(
                    "🚿 Regaderas",
                    "🔒 Casilleros",
                    "🅿️ Estacionamiento",
                    "📶 WiFi Gratis",
                    "🏋️ Equipamiento",
                    "❄️ Aire Acondicionado"
                )
            }

            // Parsear photos
            val photosJson = box[Boxes.photos]
            val photos = try {
                kotlinx.serialization.json.Json.decodeFromString<List<String>>(photosJson)
            } catch (_: Exception) {
                listOf("🏢", "🏋️", "💪", "🤸")
            }

            BoxInfoResponse(
                id = box[Boxes.id].toString(),
                name = box[Boxes.name],
                description = box[Boxes.description] ?: "El mejor BOX de CrossFit",
                address = box[Boxes.location],
                phone = box[Boxes.phone],
                email = box[Boxes.email] ?: ownerEmail,
                schedule = box[Boxes.schedule] ?: "Lun-Vie: 6:00 AM - 10:00 PM\nSáb-Dom: 8:00 AM - 6:00 PM",
                rating = box[Boxes.rating],
                totalReviews = box[Boxes.totalReviews],
                totalMembers = totalMembers,
                coaches = coaches,
                amenities = amenities,
                photos = photos
            )
        } catch (e: Exception) {
            println("Error in getBoxInfoForAthlete: ${e.message}")
            null
        }
    }

    fun getBoxProfile(userId: String): BoxProfileResponse? = transaction {
        try {
            val uuid = UUID.fromString(userId)

            val ownerEmail = Users.selectAll()
                .where { Users.id eq uuid }
                .singleOrNull()
                ?.get(Users.email) ?: ""

            // Obtener el box del owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq uuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Calcular estadísticas
            val totalMembers = Athletes.selectAll()
                .where { Athletes.boxId eq boxId }
                .count()
                .toInt()

            // Miembros activos
            val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
            val activeMembers = if (totalMembers > 0) {
                WorkoutLogs
                    .innerJoin(Athletes)
                    .selectAll()
                    .where {
                        (Athletes.boxId eq boxId) and
                                (WorkoutLogs.completedAt greaterEq thirtyDaysAgo)
                    }
                    .withDistinct()
                    .count()
                    .toInt()
            } else {
                0
            }

            // Total de coaches
            val coaches = Coaches.selectAll()
                .where { Coaches.boxId eq boxId }
                .count()
                .toInt()

            // Total de clases (schedules únicos)
            val classes = ClassSchedules.selectAll()
                .where {
                    (ClassSchedules.boxId eq boxId) and
                            (ClassSchedules.isActive eq true)
                }
                .count()
                .toInt()

            // Año de fundación
            val foundedYear = box[Boxes.createdAt].year.toString()

            val stats = listOf(
                StatItem("👥", totalMembers.toString(), "Miembros"),
                StatItem("👨‍🏫", coaches.toString(), "Coaches"),
                StatItem("📅", classes.toString(), "Clases")
            )

            BoxProfileResponse(
                id = boxId.toString(),
                name = box[Boxes.name],
                email = box[Boxes.email] ?: ownerEmail,
                phone = box[Boxes.phone],
                address = box[Boxes.location],
                foundedYear = foundedYear,
                totalMembers = totalMembers,
                activeMembers = activeMembers,
                coaches = coaches,
                classes = classes,
                rating = box[Boxes.rating],
                description = box[Boxes.description] ?: "",
                stats = stats
            )
        } catch (e: Exception) {
            println("Error in getBoxProfile: ${e.message}")
            null
        }
    }

    private fun calculateStreakForAthlete(athleteId: UUID): Int {
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

    private fun isClassNow(classTime: String, currentTime: String): Boolean {
        // Convertir "09:00 AM" a "09:00" para comparación
        val classHour = classTime.replace(" AM", "").replace(" PM", "")
        val currentHour = currentTime

        // Rango de +/- 30 minutos
        return classHour == currentHour // Simplificado, puedes mejorar la lógica
    }
}