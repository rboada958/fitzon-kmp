package com.tepuytech.fitzon.plugins

import com.tepuytech.fitzon.utils.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "fitzon"
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("id").asString().isNotEmpty()) JWTPrincipal(credential.payload) else null
            }
        }
    }
}