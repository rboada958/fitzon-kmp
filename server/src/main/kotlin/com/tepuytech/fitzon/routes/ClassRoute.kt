package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.ClassRepository
import com.tepuytech.fitzon.models.CreateClassRequest
import com.tepuytech.fitzon.models.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.classRoutes(repo: ClassRepository) {
    route("/classes") {
        authenticate("auth-jwt") {

            // 📍 POST /api/classes - Crear classInfo
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val request = try {
                    call.receive<CreateClassRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val classSchedule = repo.createClass(
                    userId,
                    request.name,
                    request.coachId,
                    request.description,
                    request.startTime,
                    request.endTime,
                    request.dayOfWeek,
                    request.maxCapacity,
                    request.level,
                    request.workoutId
                )

                if (classSchedule == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to create classInfo"))
                } else {
                    call.respond(HttpStatusCode.Created, classSchedule)
                }
            }

            // 📍 GET /api/classes/box/{boxId} - Listar classInfo del box
            get("/box/{boxId}") {
                val boxId = call.parameters["boxId"]

                if (boxId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Box ID is required"))
                    return@get
                }

                val classes = repo.getClassesByBox(boxId)
                call.respond(HttpStatusCode.OK, classes)
            }

            // 📍 DELETE /api/classes/{classId} - Eliminar classInfo
            delete("/{classId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val classId = call.parameters["classId"]

                if (userId.isNullOrEmpty() || classId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@delete
                }

                val success = repo.deleteClass(userId, classId)
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to delete classInfo"))
                }
            }

            // 📍 POST /api/classes/{classId}/enroll - Inscribirse a clase
            post("/{classId}/enroll") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val classId = call.parameters["classId"]
                if (classId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Class ID is required"))
                    return@post
                }

                val result = repo.enrollInClass(userId, classId)

                if (result == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to enroll. Class may be full or you're already enrolled"))
                } else {
                    call.respond(HttpStatusCode.OK, result)
                }
            }

            // 📍 DELETE /api/classes/{classId}/enroll - Cancelar inscription
            delete("/{classId}/enroll") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@delete
                }

                val classId = call.parameters["classId"]
                if (classId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Class ID is required"))
                    return@delete
                }

                val success = repo.cancelEnrollment(userId, classId)

                if (success) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Enrollment cancelled successfully"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to cancel enrollment"))
                }
            }

            // 📍 GET /api/classes/my-classes - Ver mis clases inscritas
            get("/my-classes") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val filter = call.request.queryParameters["filter"] ?: "all"

                val result = repo.getMyClasses(userId, filter)

                if (result == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Athlete not found"))
                } else {
                    call.respond(HttpStatusCode.OK, result)
                }
            }

            // 📍 GET /api/classes/available - Ver clases disponibles
            get("/available") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val dayOfWeek = call.request.queryParameters["dayOfWeek"]

                val result = repo.getAvailableClasses(userId, dayOfWeek)

                if (result == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Athlete or box not found"))
                } else {
                    call.respond(HttpStatusCode.OK, result)
                }
            }
        }
    }
}