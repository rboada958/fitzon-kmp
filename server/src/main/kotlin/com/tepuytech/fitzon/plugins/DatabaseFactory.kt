package com.tepuytech.fitzon.plugins

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL")
        val (jdbcUrl, user, password) = parseDatabase(databaseUrl)

        println("🔌 Connecting to database at: $jdbcUrl")

        Database.connect(
            url = jdbcUrl,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        // Solo migrar si el archivo existe
        val migrationFile = java.io.File("src/main/resources/db/migration/V1__initial_schema.sql")
        if (migrationFile.exists() && migrationFile.length() > 0) {
            val flyway = Flyway.configure()
                .dataSource(jdbcUrl, user, password)
                .locations("classpath:db/migration")
                .load()

            try {
                flyway.migrate()
                println("✅ Database migrations completed successfully")
            } catch (e: Exception) {
                println("❌ Migration error: ${e.message}")
                throw e
            }
        }
    }

    private fun parseDatabase(databaseUrl: String?): Triple<String, String, String> {
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            val url = databaseUrl.removePrefix("postgresql://")
            val userPassAndRest = url.split("@")
            val userPass = userPassAndRest[0].split(":")
            val hostPortDb = userPassAndRest[1]
            val user = userPass[0]
            val password = userPass[1]
            val hostAndDb = hostPortDb.split("/")
            val hostPort = hostAndDb[0]
            val database = hostAndDb[1]
            return Triple("jdbc:postgresql://$hostPort/$database", user, password)
        }
        return Triple(
            "jdbc:postgresql://localhost:5432/fitzon_db",
            System.getenv("DATABASE_USER") ?: "fitzon_user",
            System.getenv("DATABASE_PASSWORD") ?: "fitzon_pass"
        )
    }
}