package com.tepuytech.fitzon.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val ISSUER = "fitzon-app"
    private const val AUDIENCE = "fitzon-client"
    private const val EXPIRATION_TIME = 86400000L // 24 horas
    private val SECRET = System.getenv("JWT_SECRET") ?: "your-super-secret-key-change-in-production"

    private val algorithm = Algorithm.HMAC256(SECRET)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun makeToken(userId: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("id", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
    }
}