package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.PersonalRecordResponse
import com.tepuytech.fitzon.models.PersonalRecords
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PersonalRecordRepository {

    fun createPersonalRecord(
        userId: String,
        exerciseName: String,
        value: String,
        unit: String
    ): PersonalRecordResponse? = transaction {
        try {
            val userUuid = UUID.fromString(userId)

            // Obtener el athlete
            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            // Crear PR
            val prId = UUID.randomUUID()
            PersonalRecords.insert {
                it[PersonalRecords.id] = prId
                it[PersonalRecords.athleteId] = athleteId
                it[PersonalRecords.exerciseName] = exerciseName
                it[PersonalRecords.value] = value
                it[PersonalRecords.unit] = unit
            }

            // Obtener el PR creado
            val pr = PersonalRecords.selectAll()
                .where { PersonalRecords.id eq prId }
                .single()

            PersonalRecordResponse(
                id = prId.toString(),
                athleteId = athleteId.toString(),
                exerciseName = exerciseName,
                value = value,
                unit = unit,
                achievedAt = pr[PersonalRecords.achievedAt].toString()
            )
        } catch (e: Exception) {
            println("Error in createPersonalRecord: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getPersonalRecordsByAthlete(userId: String): List<PersonalRecordResponse> = transaction {
        try {
            val userUuid = UUID.fromString(userId)

            // Obtener el athlete
            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction emptyList()

            val athleteId = athlete[Athletes.id]

            PersonalRecords.selectAll()
                .where { PersonalRecords.athleteId eq athleteId }
                .orderBy(PersonalRecords.achievedAt to SortOrder.DESC)
                .map {
                    PersonalRecordResponse(
                        id = it[PersonalRecords.id].toString(),
                        athleteId = athleteId.toString(),
                        exerciseName = it[PersonalRecords.exerciseName],
                        value = it[PersonalRecords.value],
                        unit = it[PersonalRecords.unit],
                        achievedAt = it[PersonalRecords.achievedAt].toString()
                    )
                }
        } catch (e: Exception) {
            println("Error in getPersonalRecordsByAthlete: ${e.message}")
            emptyList()
        }
    }

    fun deletePersonalRecord(userId: String, prId: String): Boolean = transaction {
        try {
            val userUuid = UUID.fromString(userId)
            val prUuid = UUID.fromString(prId)

            // Obtener el athlete
            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction false

            val athleteId = athlete[Athletes.id]

            // Eliminar PR solo si pertenece al athlete
            PersonalRecords.deleteWhere {
                (PersonalRecords.id eq prUuid) and
                        (PersonalRecords.athleteId eq athleteId)
            } > 0
        } catch (_: Exception) {
            false
        }
    }
}