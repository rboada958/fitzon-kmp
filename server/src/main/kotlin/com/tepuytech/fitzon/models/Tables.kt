package com.tepuytech.fitzon.models

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object Users : Table("users") {
    val id = uuid("id").autoGenerate()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val name = varchar("name", 255).nullable()
    val role = varchar("role", 50).default("ATHLETE") // ATHLETE, BOX_OWNER, COACH
    val profileImageUrl = varchar("profile_image_url", 500).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Boxes : Table("boxes") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val description = varchar("description", 1000).nullable()
    val location = varchar("location", 255)
    val phone = varchar("phone", 20)
    val email = varchar("email", 255).nullable()
    val schedule = varchar("schedule", 500).nullable()
    val rating = float("rating").default(0.0f)
    val totalReviews = integer("total_reviews").default(0)
    val amenities = varchar("amenities", 1000).default("[]") // JSON array
    val photos = varchar("photos", 1000).default("[]") // JSON array
    val ownerId = uuid("owner_id").references(Users.id)
    val logoUrl = varchar("logo_url", 500).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Athletes : Table("athletes") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(Users.id)
    val boxId = uuid("box_id").references(Boxes.id).nullable()
    val age = integer("age").nullable()
    val weight = double("weight").nullable()
    val height = double("height").nullable()
    val bio = varchar("bio", 500).nullable()
    val membershipType = varchar("membership_type", 50).default("Basic") // Basic, Premium
    val status = varchar("status", 50).default("PENDING") // ACTIVE, INACTIVE, PENDING
    val paymentStatus = varchar("payment_status", 50).default("PENDING")
    val joinedAt = datetime("joined_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Coaches : Table("coaches") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(Users.id)
    val boxId = uuid("box_id").references(Boxes.id)
    val specialties = varchar("specialties", 1000).default("[]") // JSON array
    val certifications = varchar("certifications", 1000).default("[]") // JSON array
    val bio = varchar("bio", 500).nullable()
    val status = varchar("status", 50).default("ACTIVE") // ACTIVE, ON_LEAVE, INACTIVE
    val rating = float("rating").default(0.0f)
    val totalClasses = integer("total_classes").default(0)
    val yearsExperience = integer("years_experience").default(0)
    val joinedAt = datetime("joined_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Classes : Table("classes") {
    val id = uuid("id").autoGenerate()
    val boxId = uuid("box_id").references(Boxes.id)
    val coachId = uuid("coach_id").references(Coaches.id)
    val name = varchar("name", 255)
    val description = varchar("description", 500).nullable()
    val startTime = varchar("start_time", 10)
    val endTime = varchar("end_time", 10)
    val dayOfWeek = varchar("day_of_week", 10)
    val maxCapacity = integer("max_capacity")
    val level = varchar("level", 50).default("BEGINNER")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object ClassEnrollments : Table("class_enrollments") {
    val id = uuid("id").autoGenerate()
    val classId = uuid("class_id").references(ClassSchedules.id)
    val athleteId = uuid("athlete_id").references(Athletes.id)
    val enrolledAt = datetime("enrolled_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
    init {
        uniqueIndex(classId, athleteId)
    }
}

object Workouts : Table("workouts") {
    val id = uuid("id").autoGenerate()
    val classId = uuid("class_id").references(Classes.id).nullable()
    val boxId = uuid("box_id").references(Boxes.id)
    val title = varchar("title", 255)
    val description = varchar("description", 1000).nullable()
    val date = date("date")
    val dayOfWeek = varchar("day_of_week", 20)
    val duration = integer("duration")
    val difficulty = varchar("difficulty", 50)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Exercises : Table("exercises") {
    val id = uuid("id").autoGenerate()
    val workoutId = uuid("workout_id").references(Workouts.id)
    val name = varchar("name", 255)
    val sets = integer("sets")
    val reps = integer("reps")
    val weight = varchar("weight", 50).nullable()
    val notes = varchar("notes", 500).nullable()
    val videoUrl = varchar("video_url", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Notifications : Table("notifications") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(Users.id)
    val title = varchar("title", 255)
    val message = varchar("message", 1000)
    val type = varchar("type", 50)
    val isRead = bool("is_read").default(false)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object WorkoutLogs : Table("workout_logs") {
    val id = uuid("id")
    val athleteId = uuid("athlete_id").references(Athletes.id)
    val workoutId = uuid("workout_id").references(Workouts.id).nullable()
    val completedAt = datetime("completed_at").defaultExpression(CurrentDateTime)
    val caloriesBurned = integer("calories_burned").nullable()
    val durationMinutes = integer("duration_minutes").nullable()
    val notes = varchar("notes", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}

object PersonalRecords : Table("personal_records") {
    val id = uuid("id")
    val athleteId = uuid("athlete_id").references(Athletes.id)
    val exerciseName = varchar("exercise_name", 255)
    val value = varchar("value", 100)
    val unit = varchar("unit", 50)
    val achievedAt = datetime("achieved_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Achievements : Table("achievements") {
    val id = uuid("id")
    val athleteId = uuid("athlete_id").references(Athletes.id)
    val icon = varchar("icon", 10)
    val name = varchar("name", 255)
    val description = varchar("description", 500)
    val isUnlocked = bool("is_unlocked").default(false)
    val unlockedAt = datetime("unlocked_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

object ClassSchedules : Table("class_schedules") {
    val id = uuid("id").autoGenerate()
    val boxId = uuid("box_id").references(Boxes.id)
    val coachId = uuid("coach_id").references(Coaches.id)
    val name = varchar("name", 255)
    val description = varchar("description", 500).nullable()
    val time = varchar("time", 10) // HH:MM AM/PM
    val startTime = varchar("start_time", 10) // HH:MM AM/PM
    val endTime = varchar("end_time", 10) // HH:MM AM/PM
    val dayOfWeek = varchar("day_of_week", 10) // MONDAY, TUESDAY, etc
    val maxCapacity = integer("max_capacity")
    val currentEnrollment = integer("current_enrollment").default(0)
    val level = varchar("level", 50).default("BEGINNER")
    val isActive = bool("is_active").default(true)
    val workoutId = reference("workout_id", Workouts.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object MembershipRenewals : Table("membership_renewals") {
    val id = uuid("id")
    val athleteId = uuid("athlete_id").references(Athletes.id)
    val expiresAt = datetime("expires_at")
    val status = varchar("status", 50).default("PENDING") // PENDING, RENEWED, EXPIRED
    val notifiedAt = datetime("notified_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

