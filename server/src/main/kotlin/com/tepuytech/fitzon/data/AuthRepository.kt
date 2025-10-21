package com.tepuytech.fitzon.data

import at.favre.lib.crypto.bcrypt.BCrypt
import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.UserDTO
import com.tepuytech.fitzon.models.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class AuthRepository {

    fun register(
        email: String,
        password: String,
        name: String?,
        role: String,
        boxId: String? = null,
        boxName: String? = null,
        location: String? = null,
        phone: String? = null
    ): UserDTO? {
        return when (role.uppercase()) {
            "ATHLETE" -> {
                if (name == null) return null
                registerAthlete(email, password, name, boxId)
            }
            "BOX_OWNER" -> {
                if (boxName == null || location == null || phone == null) return null
                registerBoxOwner(email, password, boxName, location, phone)
            }
            else -> null
        }
    }

    fun registerAthlete(email: String, password: String, name: String, boxId: String? = null): UserDTO? = transaction {
        val exists = Users.selectAll().where { Users.email eq email }.count() > 0
        if (exists) return@transaction null

        val hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val userId = UUID.randomUUID()

        Users.insert {
            it[Users.id] = userId
            it[Users.email] = email
            it[Users.passwordHash] = hashed
            it[Users.name] = name
            it[Users.role] = "ATHLETE"
            it[Users.profileImageUrl] = null
        }

        val validBoxId = if (boxId != null) {
            val boxExists = Boxes.selectAll().where { Boxes.id eq  UUID.fromString(boxId) }.count() > 0
            if (!boxExists) {
                throw IllegalArgumentException("Box with ID $boxId does not exist.")
            }
            UUID.fromString(boxId)
        } else {
            null
        }

        // Crear perfil de atleta
        val athleteId = UUID.randomUUID()
        Athletes.insert {
            it[Athletes.id] = athleteId
            it[Athletes.userId] = userId
            if (validBoxId != null) it[Athletes.boxId] = validBoxId
        }
        UserDTO(
            id = userId.toString(),
            email = email,
            name = name,
            role = "ATHLETE",
            profileImageUrl = null,
            createdAt = null
        )
    }

    fun registerBoxOwner(
        email: String,
        password: String,
        boxName: String,
        location: String,
        phone: String
    ): UserDTO? = transaction {
        val exists = Users.selectAll().where { Users.email eq email }.count() > 0
        if (exists) return@transaction null

        val hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val userId = UUID.randomUUID()

        Users.insert {
            it[Users.id] = userId
            it[Users.email] = email
            it[Users.passwordHash] = hashed
            it[Users.name] = boxName
            it[Users.role] = "BOX_OWNER"
        }

        // Crear box
        Boxes.insert {
            it[Boxes.id] = UUID.randomUUID()
            it[Boxes.name] = boxName
            it[Boxes.location] = location
            it[Boxes.phone] = phone
            it[Boxes.ownerId] = userId
        }

        UserDTO(
            id = userId.toString(),
            email = email,
            name = boxName,
            role = "BOX_OWNER",
            profileImageUrl = null,
            createdAt = null
        )
    }

    fun login(email: String, password: String): UserDTO? = transaction {
        val userRow = Users.selectAll().where { Users.email eq email }.singleOrNull() ?: return@transaction null

        val valid = BCrypt.verifyer()
            .verify(password.toCharArray(), userRow[Users.passwordHash])
            .verified

        if (valid) {
            UserDTO(
                userRow[Users.id].toString(),
                userRow[Users.email],
                userRow[Users.name],
                userRow[Users.role],
                userRow[Users.profileImageUrl],
                userRow[Users.createdAt].toString()
            )
        } else null
    }

    fun findById(id: String): UserDTO? = transaction {
        try {
            Users.selectAll().where { Users.id eq UUID.fromString(id) }
                .map {
                    UserDTO(
                        it[Users.id].toString(),
                        it[Users.email],
                        it[Users.name],
                        it[Users.role]
                    )
                }
                .singleOrNull()
        } catch (_: Exception) {
            null
        }
    }

    fun getAll(): List<UserDTO> = transaction {
        Users.selectAll().map {
            UserDTO(
                it[Users.id].toString(),
                it[Users.email],
                it[Users.name],
                it[Users.role]
            )
        }
    }

    fun updateProfile(id: String, name: String?, profileImageUrl: String?): UserDTO? = transaction {
        try {
            val userId = UUID.fromString(id)
            val rowsUpdated = Users.update({ Users.id eq userId }) {
                if (name != null) it[Users.name] = name
                if (profileImageUrl != null) it[Users.profileImageUrl] = profileImageUrl
            }

            if (rowsUpdated > 0) findById(id) else null
        } catch (_: Exception) {
            null
        }
    }
}