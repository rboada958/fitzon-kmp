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
        Database.connect(
            url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/fitzon_db",
            driver = "org.postgresql.Driver",
            user = System.getenv("DATABASE_USER") ?: "fitzon_user",
            password = System.getenv("DATABASE_PASSWORD") ?: "fitzon_pass"
        )

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