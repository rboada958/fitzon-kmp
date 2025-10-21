package com.tepuytech.fitzon.plugins

import com.tepuytech.fitzon.models.Achievements
import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassEnrollments
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.Classes
import com.tepuytech.fitzon.models.Coaches
import com.tepuytech.fitzon.models.Exercises
import com.tepuytech.fitzon.models.MembershipRenewals
import com.tepuytech.fitzon.models.Notifications
import com.tepuytech.fitzon.models.PersonalRecords
import com.tepuytech.fitzon.models.Users
import com.tepuytech.fitzon.models.WorkoutLogs
import com.tepuytech.fitzon.models.Workouts
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL")

        if (databaseUrl != null) {
            // la URL de Render: postgresql://user:pass@host:port/dbname
            val url = if (databaseUrl.startsWith("postgresql://")) {
                databaseUrl.removePrefix("postgresql://")
            } else {
                databaseUrl.removePrefix("jdbc:postgresql://")
            }

            val userPassAndRest = url.split("@")
            val userPass = userPassAndRest[0].split(":")
            val hostPortDb = userPassAndRest[1]

            val user = userPass[0]
            val password = userPass[1]

            val hostAndDb = hostPortDb.split("/")
            val hostPort = hostAndDb[0]
            val database = hostAndDb[1]

            val jdbcUrl = "jdbc:postgresql://$hostPort/$database"

            println("Connecting to database at: $hostPort")

            Database.connect(
                url = jdbcUrl,
                driver = "org.postgresql.Driver",
                user = user,
                password = password
            )
        } else {
            Database.connect(
                url = "jdbc:postgresql://localhost:5432/fitzon_db",
                driver = "org.postgresql.Driver",
                user = System.getenv("DATABASE_USER") ?: "fitzon_user",
                password = System.getenv("DATABASE_PASSWORD") ?: "fitzon_pass"
            )
        }

        transaction {
            SchemaUtils.create(
                Users,
                Boxes,
                Athletes,
                Coaches,
                Classes,
                ClassEnrollments,
                Workouts,
                Exercises,
                Notifications,
                WorkoutLogs,
                PersonalRecords,
                Achievements,
                ClassSchedules,
                MembershipRenewals
            )
        }
    }
}