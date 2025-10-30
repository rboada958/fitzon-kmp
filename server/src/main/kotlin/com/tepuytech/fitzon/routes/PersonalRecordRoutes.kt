package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.PersonalRecordRepository
import com.tepuytech.fitzon.models.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.personalRecordRoutes(repo: PersonalRecordRepository) {
    route("/personal-records") {
        authenticate("auth-jwt") {

            // 📍 POST /api/personal-records - Crear PR
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val request = try {
                    call.receive<CreatePersonalRecordRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val pr = repo.createPersonalRecord(
                    userId,
                    request.exerciseName,
                    request.value,
                    request.unit
                )

                if (pr == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to create personal record"))
                } else {
                    call.respond(HttpStatusCode.Created, pr)
                }
            }

            // 📍 GET /api/personal-records - Listar PRs del atleta
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val prs = repo.getPersonalRecordsByAthlete(userId)
                call.respond(HttpStatusCode.OK, prs)
            }

            // 📍 DELETE /api/personal-records/{prId} - Eliminar PR
            delete("/{prId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val prId = call.parameters["prId"]

                if (userId.isNullOrEmpty() || prId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@delete
                }

                val success = repo.deletePersonalRecord(userId, prId)
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to delete personal record"))
                }
            }
        }
    }
}

