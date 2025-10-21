package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

class MemberRepository {

    fun getMembersByBox(boxId: String): List<MemberResponse> = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            Athletes
                .innerJoin(Users)
                .selectAll()
                .where { Athletes.boxId eq uuid }
                .map { row ->
                    val athleteId = row[Athletes.id]
                    val userId = row[Athletes.userId]

                    // Calcular total de workouts
                    val totalWorkouts = WorkoutLogs.selectAll()
                        .where { WorkoutLogs.athleteId eq athleteId }
                        .count()
                        .toInt()

                    // Obtener última actividad
                    val lastActivity = WorkoutLogs.selectAll()
                        .where { WorkoutLogs.athleteId eq athleteId }
                        .orderBy(WorkoutLogs.completedAt to SortOrder.DESC)
                        .limit(1)
                        .singleOrNull()
                        ?.get(WorkoutLogs.completedAt)

                    MemberResponse(
                        id = athleteId.toString(),
                        userId = userId.toString(),
                        name = row[Users.name] ?: "Atleta",
                        email = row[Users.email],
                        phone = null, // Puedes agregar phone a Users si quieres
                        membershipType = row[Athletes.membershipType],
                        status = row[Athletes.status],
                        joinedAt = formatJoinedAt(row[Athletes.joinedAt]),
                        lastActivity = formatLastActivity(lastActivity),
                        totalWorkouts = totalWorkouts,
                        paymentStatus = row[Athletes.paymentStatus]
                    )
                }
        } catch (e: Exception) {
            println("Error in getMembersByBox: ${e.message}")
            emptyList()
        }
    }

    fun updateMember(
        boxOwnerId: String,
        memberId: String,
        membershipType: String?,
        status: String?,
        paymentStatus: String?
    ): MemberResponse? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val memberUuid = UUID.fromString(memberId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Verificar que el miembro pertenece al box
            val member = Athletes.selectAll()
                .where {
                    (Athletes.id eq memberUuid) and
                            (Athletes.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction null

            // Actualizar miembro
            Athletes.update({ Athletes.id eq memberUuid }) {
                if (membershipType != null) it[Athletes.membershipType] = membershipType
                if (status != null) it[Athletes.status] = status
                if (paymentStatus != null) it[Athletes.paymentStatus] = paymentStatus
            }

            // Retornar miembro actualizado
            getMemberById(boxId.toString(), memberId)
        } catch (e: Exception) {
            println("Error in updateMember: ${e.message}")
            null
        }
    }

    fun removeMember(boxOwnerId: String, memberId: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val memberUuid = UUID.fromString(memberId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            // Remover al miembro del box
            Athletes.update({
                (Athletes.id eq memberUuid) and (Athletes.boxId eq boxId)
            }) {
                it[Athletes.boxId] = null
                it[Athletes.status] = "INACTIVE"
            } > 0
        } catch (_: Exception) {
            false
        }
    }

    fun approvePendingMember(boxOwnerId: String, memberId: String): MemberResponse? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val memberUuid = UUID.fromString(memberId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Actualizar estado a ACTIVE
            Athletes.update({
                (Athletes.id eq memberUuid) and
                        (Athletes.boxId eq boxId) and
                        (Athletes.status eq "PENDING")
            }) {
                it[Athletes.status] = "ACTIVE"
            }

            getMemberById(boxId.toString(), memberId)
        } catch (_: Exception) {
            null
        }
    }

    private fun getMemberById(boxId: String, memberId: String): MemberResponse? = transaction {
        try {
            val boxUuid = UUID.fromString(boxId)
            val memberUuid = UUID.fromString(memberId)

            Athletes
                .innerJoin(Users)
                .selectAll()
                .where {
                    (Athletes.id eq memberUuid) and
                            (Athletes.boxId eq boxUuid)
                }
                .map { row ->
                    val athleteId = row[Athletes.id]

                    val totalWorkouts = WorkoutLogs.selectAll()
                        .where { WorkoutLogs.athleteId eq athleteId }
                        .count()
                        .toInt()

                    val lastActivity = WorkoutLogs.selectAll()
                        .where { WorkoutLogs.athleteId eq athleteId }
                        .orderBy(WorkoutLogs.completedAt to SortOrder.DESC)
                        .limit(1)
                        .singleOrNull()
                        ?.get(WorkoutLogs.completedAt)

                    MemberResponse(
                        id = athleteId.toString(),
                        userId = row[Athletes.userId].toString(),
                        name = row[Users.name] ?: "Atleta",
                        email = row[Users.email],
                        phone = null,
                        membershipType = row[Athletes.membershipType],
                        status = row[Athletes.status],
                        joinedAt = formatJoinedAt(row[Athletes.joinedAt]),
                        lastActivity = formatLastActivity(lastActivity),
                        totalWorkouts = totalWorkouts,
                        paymentStatus = row[Athletes.paymentStatus]
                    )
                }
                .singleOrNull()
        } catch (_: Exception) {
            null
        }
    }

    private fun formatJoinedAt(date: LocalDateTime): String {
        val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
        return "${months[date.monthValue - 1]} ${date.year}"
    }

    private fun formatLastActivity(date: LocalDateTime?): String {
        if (date == null) return "Sin actividad"

        val now = LocalDateTime.now()
        val duration = Duration.between(date, now)

        return when {
            duration.toMinutes() < 60 -> "Hace ${duration.toMinutes()} minutos"
            duration.toHours() < 24 -> "Hace ${duration.toHours()} horas"
            duration.toDays() == 1L -> "Ayer"
            duration.toDays() < 7 -> "Hace ${duration.toDays()} días"
            duration.toDays() < 30 -> "Hace ${duration.toDays() / 7} semanas"
            else -> "Hace ${duration.toDays() / 30} meses"
        }
    }
}