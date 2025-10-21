package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.AthleteRepository
import com.tepuytech.fitzon.models.ErrorResponse
import com.tepuytech.fitzon.models.JoinBoxRequest
import com.tepuytech.fitzon.models.UpdateAthleteProfileRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


fun Route.athleteRoutes(repo: AthleteRepository) {
    route("/athletes") {

        get {
            val athletes = repo.getAllAthletes()
            call.respond(HttpStatusCode.OK, athletes)
        }

        authenticate("auth-jwt") {

            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val athlete = repo.getFullAthleteProfile(userId)
                if (athlete == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Athlete profile not found"))
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        athlete)
                }
            }

            put("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@put
                }

                val request = try {
                    call.receive<UpdateAthleteProfileRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@put
                }

                val user = repo.updateAthleteProfile(
                    userId, request.age, request.weight, request.height, request.bio
                )

                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                } else {
                    call.respond(HttpStatusCode.OK, user)
                }
            }

            get("/dashboard") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val dashboard = repo.getAthleteDashboard(userId)
                if (dashboard == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Athlete not found"))
                } else {
                    call.respond(HttpStatusCode.OK, dashboard)
                }
            }

            post("/join-box") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val request = try {
                    call.receive<JoinBoxRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val athlete = repo.joinBox(userId, request.boxId)
                if (athlete == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to join box"))
                } else {
                    call.respond(HttpStatusCode.OK, athlete)
                }
            }

            post("/leave-box") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val athlete = repo.leaveBox(userId)
                if (athlete == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Athlete not found"))
                } else {
                    call.respond(HttpStatusCode.OK, athlete)
                }
            }
        }
    }
}