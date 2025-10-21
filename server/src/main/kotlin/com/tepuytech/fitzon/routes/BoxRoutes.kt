package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.BoxRepository
import com.tepuytech.fitzon.models.ErrorResponse
import com.tepuytech.fitzon.models.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Route.boxRoutes(repo: BoxRepository) {
    route("/boxes") {

        // 📍 GET /api/boxes - Obtener todos los boxes
        get {
            val boxes = repo.getAllBoxes()
            call.respond(HttpStatusCode.OK, boxes)
        }

        // 📍 GET /api/boxes/{boxId} - Obtener un box por ID
        get("/{boxId}") {
            val boxId = call.parameters["boxId"]

            if (boxId.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Box ID is required"))
                return@get
            }

            val box = repo.getBoxById(boxId)
            if (box == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Box not found"))
            } else {
                call.respond(HttpStatusCode.OK, box)
            }
        }

        authenticate("auth-jwt") {
            // 📍 GET /api/boxes/dashboard - Dashboard del Box Owner
            get("/dashboard") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                // Verificar que el usuario es BOX_OWNER
                val user = transaction {
                    Users.selectAll().where { Users.id eq UUID.fromString(userId) }.singleOrNull()
                }

                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                    return@get
                }

                if (user[Users.role] != "BOX_OWNER") {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("User is not a box owner. Current role: ${user[Users.role]}"))
                    return@get
                }

                val dashboard = repo.getBoxDashboard(userId)
                if (dashboard == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Box not found for this owner"))
                } else {
                    call.respond(HttpStatusCode.OK, dashboard)
                }
            }

            get("/{boxId}/info") {
                val boxId = call.parameters["boxId"]

                if (boxId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Box ID is required"))
                    return@get
                }

                val boxInfo = repo.getBoxInfoForAthlete(boxId)
                if (boxInfo == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Box not found"))
                } else {
                    call.respond(HttpStatusCode.OK, boxInfo)
                }
            }

            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                // Verificar que el usuario es BOX_OWNER
                val user = transaction {
                    Users.selectAll().where { Users.id eq UUID.fromString(userId) }.singleOrNull()
                }

                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                    return@get
                }

                if (user[Users.role] != "BOX_OWNER") {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("User is not a box owner"))
                    return@get
                }

                val profile = repo.getBoxProfile(userId)
                if (profile == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Box profile not found"))
                } else {
                    call.respond(HttpStatusCode.OK, profile)
                }
            }
        }
    }
}