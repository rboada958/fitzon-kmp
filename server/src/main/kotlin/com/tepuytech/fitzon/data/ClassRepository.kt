package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ClassRepository {

    fun createClass(
        boxOwnerId: String,
        name: String,
        coachId: String,
        description: String?,
        startTime: String,
        endTime: String,
        dayOfWeek: String,
        maxCapacity: Int,
        level: String
    ): ClassScheduleItem? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val coachUuid = UUID.fromString(coachId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Verificar que el coach pertenece al box
            val coach = Coaches
                .innerJoin(Users)
                .selectAll()
                .where {
                    (Coaches.id eq coachUuid) and
                            (Coaches.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction null

            val coachName = coach[Users.name] ?: "Coach"

            // Crear clase
            val classId = UUID.randomUUID()
            ClassSchedules.insert {
                it[ClassSchedules.id] = classId
                it[ClassSchedules.boxId] = boxId
                it[ClassSchedules.coachId] = coachUuid
                it[ClassSchedules.name] = name
                it[ClassSchedules.description] = description
                it[ClassSchedules.time] = startTime
                it[ClassSchedules.startTime] = startTime
                it[ClassSchedules.endTime] = endTime
                it[ClassSchedules.dayOfWeek] = dayOfWeek.uppercase()
                it[ClassSchedules.maxCapacity] = maxCapacity
                it[ClassSchedules.level] = level
                it[ClassSchedules.isActive] = true
            }

            ClassScheduleItem(
                id = classId.toString(),
                time = startTime,
                name = name,
                coachName = coachName,
                currentEnrollment = 0,
                maxCapacity = maxCapacity,
                isNow = false
            )
        } catch (e: Exception) {
            println("Error in createClass: ${e.message}")
            null
        }
    }

    fun getClassesByBox(boxId: String): List<ClassScheduleItem> = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            ClassSchedules.selectAll()
                .where {
                    (ClassSchedules.boxId eq uuid) and
                            (ClassSchedules.isActive eq true)
                }
                .orderBy(ClassSchedules.dayOfWeek)
                .orderBy(ClassSchedules.startTime)
                .map { schedule : ResultRow ->
                    val coachId = schedule[ClassSchedules.coachId]
                    val coach = Coaches.innerJoin(Users)
                        .selectAll()
                        .where { Coaches.id eq coachId }
                        .singleOrNull()

                    // Contar enrollments
                    val currentEnrollment = ClassEnrollments.selectAll()
                        .where { ClassEnrollments.classId eq schedule[ClassSchedules.id] }
                        .count()
                        .toInt()

                    ClassScheduleItem(
                        id = schedule[ClassSchedules.id].toString(),
                        time = schedule[ClassSchedules.startTime],
                        name = schedule[ClassSchedules.name],
                        coachName = coach?.get(Users.name) ?: "Coach",
                        currentEnrollment = currentEnrollment,
                        maxCapacity = schedule[ClassSchedules.maxCapacity],
                        isNow = false
                    )
                }
        } catch (e: Exception) {
            println("Error in getClassesByBox: ${e.message}")
            emptyList()
        }
    }

    fun deleteClass(boxOwnerId: String, classId: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val classUuid = UUID.fromString(classId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            // Soft delete - marcar como inactiva
            ClassSchedules.update({
                (ClassSchedules.id eq classUuid) and
                        (ClassSchedules.boxId eq boxId)
            }) {
                it[ClassSchedules.isActive] = false
            } > 0
        } catch (_: Exception) {
            false
        }
    }
}