package com.tepuytech.fitzon.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

object JwtConfig {
    private const val ISSUER = "fitzon-app"
    private const val AUDIENCE = "fitzon-client"

    private const val ACCESS_TOKEN_EXPIRATION_TIME = 900000L // 15 minutos
    private const val REFRESH_TOKEN_EXPIRATION_TIME = 604800000L // 7 días

    private const val SECRET = "your-super-secret-key-change-in-production"
    private const val REFRESH_SECRET = "your-super-secret-refresh-key-change-in-production"

    private val algorithm = Algorithm.HMAC256(SECRET)
    private val refreshAlgorithm = Algorithm.HMAC256(REFRESH_SECRET)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    val refreshVerifier: JWTVerifier = JWT.require(refreshAlgorithm)
        .withIssuer(ISSUER)
        .build()

    // Data class para retornar ambos tokens
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String,
        val expiresIn: Long = ACCESS_TOKEN_EXPIRATION_TIME
    )

    /**
     * Genera un par de tokens (access token y refresh token)
     * @param userId ID del usuario
     * @return TokenPair con ambos tokens
     */
    fun generateTokenPair(userId: String): TokenPair {
        val accessToken = makeAccessToken(userId)
        val refreshToken = makeRefreshToken(userId)

        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = ACCESS_TOKEN_EXPIRATION_TIME
        )
    }

    /**
     * Genera un access token (corta duración)
     * @param userId ID del usuario
     * @return JWT token como String
     */
    fun makeAccessToken(userId: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("id", userId)
            .withClaim("type", "access")
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
            .sign(algorithm)
    }

    /**
     * Genera un refresh token (large duración)
     * @param userId ID del usuario
     * @return JWT token como String
     */
    fun makeRefreshToken(userId: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withClaim("id", userId)
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
            .sign(refreshAlgorithm)
    }

    /**
     * Obtiene el ID del usuario de un refresh token
     * @param token JWT refresh token
     * @return ID del usuario o null si es inválido
     */
    fun getUserIdFromRefreshToken(token: String): String? {
        return try {
            val decoded = refreshVerifier.verify(token)
            decoded.getClaim("id").asString()
        } catch (_: JWTVerificationException) {
            null
        }
    }
}