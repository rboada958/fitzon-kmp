package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class CoachRepository {

    fun getCoachesByBox(boxId: String): List<CoachResponse> = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            Coaches
                .innerJoin(Users)
                .selectAll()
                .where { Coaches.boxId eq uuid }
                .map { row ->
                    val specialtiesJson = row[Coaches.specialties]
                    val specialties = try {
                        Json.decodeFromString<List<String>>(specialtiesJson)
                    } catch (_: Exception) {
                        emptyList()
                    }

                    val certificationsJson = row[Coaches.certifications]
                    val certifications = try {
                        Json.decodeFromString<List<String>>(certificationsJson)
                    } catch (_: Exception) {
                        emptyList()
                    }

                    // Contar clases actuales del coach esta semana
                    val coachId = row[Coaches.id]

                    val currentClasses = ClassSchedules.selectAll()
                        .where {
                            (ClassSchedules.coachId eq coachId) and
                                    (ClassSchedules.isActive eq true)
                        }
                        .count()
                        .toInt()

                    CoachResponse(
                        id = row[Coaches.id].toString(),
                        userId = row[Coaches.userId].toString(),
                        name = row[Users.name] ?: "Coach",
                        email = row[Users.email],
                        phone = row[Users.profileImageUrl], // Modify to get phone from Users table
                        specialties = specialties,
                        certifications = certifications,
                        status = row[Coaches.status],
                        joinedAt = formatJoinedAt(row[Coaches.joinedAt]),
                        totalClasses = row[Coaches.totalClasses],
                        rating = row[Coaches.rating],
                        currentClasses = currentClasses,
                        yearsExperience = "${row[Coaches.yearsExperience]} años"
                    )
                }
        } catch (e: Exception) {
            println("Error in getCoachesByBox: ${e.message}")
            emptyList()
        }
    }

    fun promoteAthleteToCoach(
        boxOwnerId: String,
        athleteId: String,
        specialties: List<String>,
        certifications: List<String>,
        yearsExperience: Int
    ): CoachResponse? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val athleteUuid = UUID.fromString(athleteId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Verificar que el atleta pertenece al box
            val athlete = Athletes.selectAll()
                .where {
                    (Athletes.id eq athleteUuid) and
                            (Athletes.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction null

            val userId = athlete[Athletes.userId]

            // Verificar que no sea ya coach
            val existingCoach = Coaches.selectAll()
                .where {
                    (Coaches.userId eq userId) and
                            (Coaches.boxId eq boxId)
                }
                .singleOrNull()

            if (existingCoach != null) {
                return@transaction null // Ya es coach
            }

            // Cambiar rol del usuario a COACH
            Users.update({ Users.id eq userId }) {
                it[Users.role] = "COACH"
            }

            // Crear registro de coach
            val coachId = UUID.randomUUID()
            Coaches.insert {
                it[Coaches.id] = coachId
                it[Coaches.userId] = userId
                it[Coaches.boxId] = boxId
                it[Coaches.specialties] = Json.encodeToString(specialties)
                it[Coaches.certifications] = Json.encodeToString(certifications)
                it[Coaches.yearsExperience] = yearsExperience
                it[Coaches.status] = "ACTIVE"
            }

            // Retornar el coach creado
            val user = Users.selectAll()
                .where { Users.id eq userId }
                .singleOrNull() ?: return@transaction null

            CoachResponse(
                id = coachId.toString(),
                userId = userId.toString(),
                name = user[Users.name] ?: "Coach",
                email = user[Users.email],
                phone = null,
                specialties = specialties,
                certifications = certifications,
                status = "ACTIVE",
                joinedAt = formatJoinedAt(LocalDateTime.now()),
                totalClasses = 0,
                rating = 0.0f,
                currentClasses = 0,
                yearsExperience = "$yearsExperience años"
            )
        } catch (e: Exception) {
            println("Error in promoteAthleteToCoach: ${e.message}")
            null
        }
    }

    fun updateCoachStatus(boxOwnerId: String, coachId: String, status: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val coachUuid = UUID.fromString(coachId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            // Actualizar estado del coach
            val updated = Coaches.update({
                (Coaches.id eq coachUuid) and (Coaches.boxId eq boxId)
            }) {
                it[Coaches.status] = status
            }

            updated > 0
        } catch (_: Exception) {
            false
        }
    }

    fun removeCoach(boxOwnerId: String, coachId: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val coachUuid = UUID.fromString(coachId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            // Obtener el coach
            val coach = Coaches.selectAll()
                .where {
                    (Coaches.id eq coachUuid) and (Coaches.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction false

            val userId = coach[Coaches.userId]

            // Cambiar rol de vuelta a ATHLETE
            Users.update({ Users.id eq userId }) {
                it[Users.role] = "ATHLETE"
            }

            // Eliminar coach
            Coaches.deleteWhere {
                (Coaches.id eq coachUuid) and (Coaches.boxId eq boxId)
            } > 0
        } catch (_: Exception) {
            false
        }
    }

    private fun formatJoinedAt(date: LocalDateTime): String {
        val months = listOf(
            "Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
        )
        return "${months[date.monthValue - 1]} ${date.year}"
    }
}