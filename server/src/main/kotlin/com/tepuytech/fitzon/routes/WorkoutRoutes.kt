package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.WorkoutRepository
import com.tepuytech.fitzon.models.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.workoutRoutes(repo: WorkoutRepository) {
    route("/workouts") {
        authenticate("auth-jwt") {

            // 📍 POST /api/workouts - Crear workout (BOX_OWNER o COACH)
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val request = try {
                    call.receive<CreateWorkoutRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val workout = repo.createWorkout(
                    userId,
                    request.title,
                    request.description,
                    request.date,
                    request.dayOfWeek,
                    request.duration,
                    request.difficulty,
                    request.classId,
                    request.exercises
                )

                if (workout == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to create workout"))
                } else {
                    call.respond(HttpStatusCode.Created, workout)
                }
            }

            // 📍 GET /api/workouts/{workoutId} - Obtener workout por ID
            get("/{workoutId}") {
                val workoutId = call.parameters["workoutId"]

                if (workoutId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Workout ID is required"))
                    return@get
                }

                val workout = repo.getWorkoutById(workoutId)
                if (workout == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Workout not found"))
                } else {
                    call.respond(HttpStatusCode.OK, workout)
                }
            }

            // 📍 GET /api/workouts/box/{boxId} - Listar workouts del box
            get("/box/{boxId}") {
                val boxId = call.parameters["boxId"]

                if (boxId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Box ID is required"))
                    return@get
                }

                val workouts = repo.getWorkoutsByBox(boxId)
                call.respond(HttpStatusCode.OK, workouts)
            }

            // 📍 DELETE /api/workouts/{workoutId} - Eliminar workout
            delete("/{workoutId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val workoutId = call.parameters["workoutId"]

                if (userId.isNullOrEmpty() || workoutId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@delete
                }

                val success = repo.deleteWorkout(userId, workoutId)
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to delete workout"))
                }
            }

            // 📍 GET /api/workouts/today - Obtener WOD del día
            get("/today") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val workout = repo.getWorkoutOfDay(userId)
                if (workout == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("No workout scheduled for today"))
                } else {
                    call.respond(HttpStatusCode.OK, workout)
                }
            }

            // 📍 POST /api/workouts/{workoutId}/complete - Registrar workout completado
            post("/{workoutId}/complete") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@post
                }

                val workoutId = call.parameters["workoutId"]
                if (workoutId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Workout ID is required"))
                    return@post
                }

                val request = try {
                    call.receive<CompleteWorkoutRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@post
                }

                val result = repo.completeWorkout(
                    userId = userId,
                    workoutId = workoutId,
                    caloriesBurned = request.caloriesBurned,
                    durationMinutes = request.durationMinutes,
                    notes = request.notes
                )

                if (result == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to complete workout"))
                } else {
                    call.respond(HttpStatusCode.OK, result)
                }
            }
        }
    }
}