package com.tepuytech.fitzon

import com.tepuytech.fitzon.data.AthleteRepository
import com.tepuytech.fitzon.data.AuthRepository
import com.tepuytech.fitzon.data.BoxRepository
import com.tepuytech.fitzon.data.CoachRepository
import com.tepuytech.fitzon.data.MemberRepository
import com.tepuytech.fitzon.plugins.DatabaseFactory
import com.tepuytech.fitzon.plugins.configureSecurity
import com.tepuytech.fitzon.routes.athleteRoutes
import com.tepuytech.fitzon.routes.authRoutes
import com.tepuytech.fitzon.routes.boxRoutes
import com.tepuytech.fitzon.routes.coachRoutes
import com.tepuytech.fitzon.routes.memberRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    DatabaseFactory.init()
    configureSecurity()

    routing {
        get("/") { call.respondText("Fitzon backend up ✅") }

        route("/api") {
            authRoutes(AuthRepository())
            athleteRoutes(AthleteRepository())
            boxRoutes(BoxRepository())
            coachRoutes(CoachRepository())
            memberRoutes(MemberRepository())
        }
    }
}