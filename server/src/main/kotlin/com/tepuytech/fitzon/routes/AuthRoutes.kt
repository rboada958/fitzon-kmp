package com.tepuytech.fitzon.routes

import com.tepuytech.fitzon.data.AuthRepository
import com.tepuytech.fitzon.models.AuthRequest
import com.tepuytech.fitzon.models.AuthResponse
import com.tepuytech.fitzon.models.ErrorResponse
import com.tepuytech.fitzon.models.RefreshTokenRequest
import com.tepuytech.fitzon.models.RegisterRequest
import com.tepuytech.fitzon.models.TokenResponse
import com.tepuytech.fitzon.models.UpdateProfileRequest
import com.tepuytech.fitzon.models.UserResponse
import com.tepuytech.fitzon.utils.JwtConfig
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


fun Route.authRoutes(repo: AuthRepository) {
    route("/auth") {

        post("/register") {
            val request = try {
                call.receive<RegisterRequest>()
            } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                return@post
            }

            if (!isValidEmail(request.email)) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid email format"))
                return@post
            }

            if (request.password.length < 6) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Password must be at least 6 characters"))
                return@post
            }

            val user = repo.register(
                email = request.email,
                password = request.password,
                name = request.name,
                role = request.role,
                boxId = request.boxId,
                boxName = request.boxName,
                location = request.location,
                phone = request.phone
            )

            if (user == null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("User already exists or invalid data"))
            } else {
                val tokens = JwtConfig.generateTokenPair(user.id)  // ← Cambio aquí
                call.respond(HttpStatusCode.Created, AuthResponse(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                    user = user
                ))
            }
        }

        post("/login") {
            val request = try {
                call.receive<AuthRequest>()
            } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                return@post
            }

            val user = repo.login(request.email, request.password)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid credentials"))
            } else {
                val tokens = JwtConfig.generateTokenPair(user.id)  // ← Cambio aquí
                call.respond(
                    HttpStatusCode.OK,
                    AuthResponse(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken,
                        user = user
                    )
                )
            }
        }

        post("/refresh") {
            val request = try {
                call.receive<RefreshTokenRequest>()
            } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                return@post
            }

            val userId = JwtConfig.getUserIdFromRefreshToken(request.refreshToken)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid or expired refresh token"))
                return@post
            }

            val newTokens = JwtConfig.generateTokenPair(userId)
            call.respond(
                HttpStatusCode.OK,
                TokenResponse(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    expiresIn = newTokens.expiresIn
                )
            )
        }

        authenticate("auth-jwt") {

            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("id")?.asString()

                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
                    return@get
                }

                val user = repo.findById(userId)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                } else {
                    call.respond(HttpStatusCode.OK, UserResponse(user))
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
                    call.receive<UpdateProfileRequest>()
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request format"))
                    return@put
                }

                val user = repo.updateProfile(userId, request.name, request.profileImageUrl)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                } else {
                    call.respond(HttpStatusCode.OK, user)
                }
            }
        }

        get("/users") {
            val users = repo.getAll()
            call.respond(HttpStatusCode.OK, users)
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(email)
}
