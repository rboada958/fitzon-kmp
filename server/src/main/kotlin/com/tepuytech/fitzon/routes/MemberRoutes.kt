package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.MemberRepository
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

fun Route.memberRoutes(repo: MemberRepository) {
    route("/members") {
        authenticate("auth-jwt") {

            // 📍 GET /api/members - Miembros del box
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

                val members = repo.getMembersByBox(boxId)
                call.respond(HttpStatusCode.OK, members)
            }

            // 📍 PUT /api/members/{memberId} - Actualizar miembro
            put("/{memberId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val memberId = call.parameters["memberId"]

                if (userId.isNullOrEmpty() || memberId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@put
                }

                val request = try {
                    call.receive<UpdateMemberRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@put
                }

                val member = repo.updateMember(
                    userId,
                    memberId,
                    request.membershipType,
                    request.status,
                    request.paymentStatus
                )

                if (member == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Member not found"))
                } else {
                    call.respond(HttpStatusCode.OK, member)
                }
            }

            // 📍 DELETE /api/members/{memberId} - Remover miembro del box
            delete("/{memberId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val memberId = call.parameters["memberId"]

                if (userId.isNullOrEmpty() || memberId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@delete
                }

                val success = repo.removeMember(userId, memberId)
                if (success) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Failed to remove member"))
                }
            }

            // 📍 POST /api/members/{memberId}/approve - Aprobar miembro pendiente
            post("/{memberId}/approve") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()
                val memberId = call.parameters["memberId"]

                if (userId.isNullOrEmpty() || memberId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid parameters"))
                    return@post
                }

                val member = repo.approvePendingMember(userId, memberId)
                if (member == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Member not found"))
                } else {
                    call.respond(HttpStatusCode.OK, member)
                }
            }
        }
    }
}