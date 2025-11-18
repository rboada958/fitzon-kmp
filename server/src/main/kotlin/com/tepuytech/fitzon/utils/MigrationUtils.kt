package com.tepuytech.fitzon.utils

import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassEnrollments
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.Coaches
import com.tepuytech.fitzon.models.Exercises
import com.tepuytech.fitzon.models.MembershipRenewals
import com.tepuytech.fitzon.models.Notifications
import com.tepuytech.fitzon.models.PersonalRecords
import com.tepuytech.fitzon.models.Users
import com.tepuytech.fitzon.models.WorkoutLogs
import com.tepuytech.fitzon.models.Workouts
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object MigrationGenerator {
    fun generateInitialMigration() {
        try {
            transaction {
                // Generar los statements SQL
                val statements = SchemaUtils.createStatements(
                    Users,
                    Boxes,
                    Athletes,
                    Coaches,
                    ClassEnrollments,
                    Workouts,
                    Exercises,
                    Notifications,
                    WorkoutLogs,
                    PersonalRecords,
                    ClassSchedules,
                    MembershipRenewals
                )

                println("📝 Total statements: ${statements.size}")
                statements.forEach { println("  - $it") }

                if (statements.isNotEmpty()) {
                    val sqlContent = statements.joinToString(";\n") + ";"

                    val file = File("src/main/resources/db/migration/V1__initial_schema.sql")
                    file.parentFile?.mkdirs()
                    file.writeText(sqlContent)

                    println("✅ Migration generated at: ${file.absolutePath}")
                    println("📄 File size: ${file.length()} bytes")
                } else {
                    println("⚠️ No statements were generated!")
                }
            }
        } catch (e: Exception) {
            println("❌ Error generating migration: ${e.message}")
            e.printStackTrace()
        }
    }
}