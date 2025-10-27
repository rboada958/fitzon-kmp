plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("org.flywaydb.flyway") version "11.14.0"
    kotlin("plugin.serialization") version "1.9.0"
    application
}

group = "com.tepuytech.fitzon"
version = "1.0.0"
application {
    mainClass.set("com.tepuytech.fitzon.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.call.logging)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    implementation(libs.bcrypt)

    implementation(libs.exposed.java.time)
}