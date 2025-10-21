package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.CoachRepository
import com.tepuytech.fitzon.models.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Route.coachRoutes(repo: CoachRepository) {
    route("/coaches") {
        authenticate("auth-jwt") {

            // 📍 GET /api/coaches
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                // Obtener boxId del owner
                val boxId = transaction {
                    Boxes.selectAll()
                        .where { Boxes.ownerId eq UUID.fromString(userId) }
                        .singleOrNull()
                        ?.get(Boxes.id)
                        ?.toString()
                }

                if (boxId == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Box not found"))
                    return@get
                }

                val coaches = repo.getCoachesByBox(boxId)
                call.respond(HttpStatusCode.OK, coaches)
            }

            // 📍 POST /api/coaches/promote
            post("/promote") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val request = try {
                    call.receive<PromoteToCoachRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val coach = repo.promoteAthleteToCoach(
                    userId,
                    request.athleteId,
                    request.specialties,
                    request.certifications,
                    request.yearsExperience
                )

                if (coach == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to promote athlete to coach"))
                } else {
                    call.respond(HttpStatusCode.Created, coach)
                }
            }

            // 📍 PUT /api/coaches/{coachId}/status
            put("/{coachId}/status") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val coachId = call.parameters["coachId"]

                if (userId.isNullOrEmpty() || coachId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@put
                }

                val request = try {
                    call.receive<UpdateCoachStatusRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@put
                }

                val success = repo.updateCoachStatus(userId, coachId, request.status)
                if (success) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Coach status updated"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to update coach status"))
                }
            }

            // 📍 DELETE /api/coaches/{coachId} - Remover coach
            delete("/{coachId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val coachId = call.parameters["coachId"]

                if (userId.isNullOrEmpty() || coachId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@delete
                }

                val success = repo.removeCoach(userId, coachId)
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to remove coach"))
                }
            }
        }
    }
}