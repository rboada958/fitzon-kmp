package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.AvailableClassDTO
import com.tepuytech.fitzon.models.AvailableClassesResponse
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassDetailsDTO
import com.tepuytech.fitzon.models.ClassEnrollmentInfo
import com.tepuytech.fitzon.models.ClassEnrollments
import com.tepuytech.fitzon.models.ClassScheduleItem
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.CoachInfoDTO
import com.tepuytech.fitzon.models.Coaches
import com.tepuytech.fitzon.models.EditClassResponse
import com.tepuytech.fitzon.models.EnrolledAthleteDTO
import com.tepuytech.fitzon.models.EnrollmentDTO
import com.tepuytech.fitzon.models.EnrollmentResponse
import com.tepuytech.fitzon.models.ExerciseDTO
import com.tepuytech.fitzon.models.Exercises
import com.tepuytech.fitzon.models.MyClassesResponse
import com.tepuytech.fitzon.models.TodayClassDTO
import com.tepuytech.fitzon.models.UpcomingClassDTO
import com.tepuytech.fitzon.models.Users
import com.tepuytech.fitzon.models.WorkoutDetailsDTO
import com.tepuytech.fitzon.models.WorkoutLogs
import com.tepuytech.fitzon.models.WorkoutPreviewDTO
import com.tepuytech.fitzon.models.Workouts
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

class ClassRepository {

    fun createClass(
        boxOwnerId: String,
        name: String,
        coachId: String,
        description: String?,
        startTime: String,
        endTime: String,
        dayOfWeek: String,
        maxCapacity: Int,
        level: String,
        workoutId: String? = null
    ): ClassScheduleItem? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val coachUuid = UUID.fromString(coachId)

            // Convertir workoutId si existe
            val workoutUuidOrNull = workoutId?.let { UUID.fromString(it) }

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Verificar que el coach pertenece al box
            val coach = Coaches
                .innerJoin(Users)
                .selectAll()
                .where {
                    (Coaches.id eq coachUuid) and
                            (Coaches.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction null

            val coachName = coach[Users.name] ?: "Coach"

            // Crear clase
            val classId = UUID.randomUUID()
            ClassSchedules.insert {
                it[ClassSchedules.id] = classId
                it[ClassSchedules.boxId] = boxId
                it[ClassSchedules.coachId] = coachUuid
                it[ClassSchedules.name] = name
                it[ClassSchedules.description] = description
                it[ClassSchedules.time] = startTime
                it[ClassSchedules.startTime] = startTime
                it[ClassSchedules.endTime] = endTime
                it[ClassSchedules.dayOfWeek] = dayOfWeek.uppercase()
                it[ClassSchedules.maxCapacity] = maxCapacity
                it[ClassSchedules.level] = level
                it[ClassSchedules.isActive] = true
                it[ClassSchedules.workoutId] = workoutUuidOrNull
            }

            ClassScheduleItem(
                id = classId.toString(),
                time = startTime,
                name = name,
                coachName = coachName,
                currentEnrollment = 0,
                maxCapacity = maxCapacity,
                description = description ?: "",
                level = level,
                dayOfWeek = dayOfWeek,
                isNow = false
            )
        } catch (e: Exception) {
            println("Error in createClass: ${e.message}")
            null
        }
    }

    fun getClassesByBox(boxId: String): List<ClassScheduleItem> = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            val classes = ClassSchedules.selectAll()
                .where { ClassSchedules.boxId eq uuid }
                .map { schedule : ResultRow ->
                    val coachId = schedule[ClassSchedules.coachId]
                    val coach = Coaches.innerJoin(Users)
                        .selectAll()
                        .where { Coaches.id eq coachId }
                        .singleOrNull()

                    // Contar enrollments
                    val currentEnrollment = ClassEnrollments.selectAll()
                        .where { ClassEnrollments.classId eq schedule[ClassSchedules.id] }
                        .count()
                        .toInt()

                    ClassScheduleItem(
                        id = schedule[ClassSchedules.id].toString(),
                        time = schedule[ClassSchedules.startTime],
                        name = schedule[ClassSchedules.name],
                        coachName = coach?.get(Users.name) ?: "Coach",
                        currentEnrollment = currentEnrollment,
                        maxCapacity = schedule[ClassSchedules.maxCapacity],
                        description = schedule[ClassSchedules.description] ?: "",
                        level = schedule[ClassSchedules.level],
                        dayOfWeek = schedule[ClassSchedules.dayOfWeek],
                        isNow = false
                    )
                }

            val dayOrder = mapOf(
                "MONDAY" to 1,
                "TUESDAY" to 2,
                "WEDNESDAY" to 3,
                "THURSDAY" to 4,
                "FRIDAY" to 5,
                "SATURDAY" to 6,
                "SUNDAY" to 7
            )

            classes.sortedWith(
                compareBy(
                    { dayOrder[it.dayOfWeek] ?: 8 },  // Order por día
                    { parseTime(it.time) }  // Ahora por hora
                )
            )

        } catch (e: Exception) {
            println("Error in getClassesByBox: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    fun deleteClass(boxOwnerId: String, classId: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val classUuid = UUID.fromString(classId)

            // Verificar que el box pertenece al owner
            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            val deletedRows = ClassSchedules.deleteWhere {
                (ClassSchedules.id eq classUuid) and
                        (ClassSchedules.boxId eq boxId)
            }

            deletedRows > 0

        } catch (e: Exception) {
            println("Error deleting class: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun enrollInClass(userId: String, classId: String): EnrollmentResponse? = transaction {
        try {
            val userUuid = UUID.fromString(userId)
            val classUuid = UUID.fromString(classId)

            // Obtener el athleteId
            val athlete = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            // Verificar que la clase existe
            val classRow = ClassSchedules
                .selectAll()
                .where { ClassSchedules.id eq classUuid }
                .singleOrNull() ?: return@transaction null

            // Verificar que no está llena
            val currentEnrollment = classRow[ClassSchedules.currentEnrollment]
            val maxCapacity = classRow[ClassSchedules.maxCapacity]

            if (currentEnrollment >= maxCapacity) {
                return@transaction null  // Clase llena
            }

            // Verificar que no está ya inscrito
            val alreadyEnrolled = ClassEnrollments
                .selectAll()
                .where {
                    (ClassEnrollments.classId eq classUuid) and
                            (ClassEnrollments.athleteId eq athleteId)
                }
                .count() > 0

            if (alreadyEnrolled) {
                return@transaction null  // Ya inscrito
            }

            // Crear inscription
            val enrollmentId = UUID.randomUUID()

            ClassEnrollments.insert {
                it[ClassEnrollments.id] = enrollmentId
                it[ClassEnrollments.classId] = classUuid
                it[ClassEnrollments.athleteId] = athleteId
            }

            // Incremental currentEnrollment
            ClassSchedules.update({ ClassSchedules.id eq classUuid }) {
                it[ClassSchedules.currentEnrollment] = currentEnrollment + 1
            }

            // Obtener info del coach
            val coach = Coaches
                .innerJoin(Users)
                .selectAll()
                .where { Coaches.id eq classRow[ClassSchedules.coachId] }
                .singleOrNull()

            val coachName = coach?.get(Users.name) ?: "Coach"

            EnrollmentResponse(
                message = "Enrolled successfully",
                enrollment = EnrollmentDTO(
                    id = enrollmentId.toString(),
                    classId = classId,
                    athleteId = athleteId.toString(),
                    enrolledAt = LocalDateTime.now().toString()
                ),
                classInfo = ClassEnrollmentInfo(
                    id = classId,
                    name = classRow[ClassSchedules.name],
                    dayOfWeek = classRow[ClassSchedules.dayOfWeek],
                    startTime = classRow[ClassSchedules.startTime],
                    endTime = classRow[ClassSchedules.endTime],
                    coachName = coachName,
                    currentEnrollment = currentEnrollment + 1,
                    maxCapacity = maxCapacity
                )
            )

        } catch (e: Exception) {
            println("Error enrolling in classInfo: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun cancelEnrollment(userId: String, classId: String): Boolean = transaction {
        try {
            val userUuid = UUID.fromString(userId)
            val classUuid = UUID.fromString(classId)

            // Obtener el athleteId
            val athlete = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction false

            val athleteId = athlete[Athletes.id]

            // Eliminar inscription
            ClassEnrollments.deleteWhere {
                (ClassEnrollments.classId eq classUuid) and
                        (ClassEnrollments.athleteId eq athleteId)
            }

            // Decrement currentEnrollment
            ClassSchedules.update({ ClassSchedules.id eq classUuid }) {
                with(SqlExpressionBuilder) {
                    it[currentEnrollment] = currentEnrollment - 1
                }
            }

            true

        } catch (e: Exception) {
            println("Error cancelling enrollment: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun getMyClasses(userId: String): MyClassesResponse? = transaction {
        try {
            val userUuid = UUID.fromString(userId)

            // Obtener athleteId
            val athlete = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            // Obtener todas las clases inscritas
            val enrolledClasses = (ClassEnrollments innerJoin ClassSchedules)
                .innerJoin(Coaches)
                .innerJoin(Users)
                .selectAll()
                .where {
                    (ClassEnrollments.athleteId eq athleteId) and
                            (Coaches.id eq ClassSchedules.coachId) and
                            (Users.id eq Coaches.userId)
                }
                .map { row ->
                    val dayOfWeek = row[ClassSchedules.dayOfWeek]
                    val workoutId = row[ClassSchedules.workoutId]

                    // Obtener workout si existe
                    val workout = if (workoutId != null) {
                        Workouts
                            .selectAll()
                            .where { Workouts.id eq workoutId }
                            .singleOrNull()
                            ?.let { w ->
                                // Verificar si ya completó este workout hoy
                                val isCompleted = WorkoutLogs
                                    .selectAll()
                                    .where {
                                        (WorkoutLogs.athleteId eq athleteId) and
                                                (WorkoutLogs.workoutId eq workoutId) and
                                                (WorkoutLogs.completedAt greaterEq LocalDateTime.now().toLocalDate().atStartOfDay())
                                    }
                                    .count() > 0

                                WorkoutPreviewDTO(
                                    id = w[Workouts.id].toString(),
                                    title = w[Workouts.title],
                                    difficulty = w[Workouts.difficulty],
                                    duration = w[Workouts.duration],
                                    isCompleted = isCompleted
                                )
                            }
                    } else null

                    Triple(
                        row,
                        dayOfWeek,
                        workout
                    )
                }

            // Clases de hoy
            val todayDayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).uppercase()
            val todayClasses = enrolledClasses
                .filter { it.second == todayDayOfWeek }
                .map { (row, _, workout) ->
                    TodayClassDTO(
                        classId = row[ClassSchedules.id].toString(),
                        className = row[ClassSchedules.name],
                        startTime = row[ClassSchedules.startTime],
                        endTime = row[ClassSchedules.endTime],
                        coachName = row[Users.name] ?: "Coach",
                        workout = workout,
                        status = "upcoming"
                    )
                }


            val upcomingClasses = enrolledClasses
                .filter { it.second != todayDayOfWeek }
                .take(5)
                .map { (row, dayOfWeek, workout) ->
                    UpcomingClassDTO(
                        classId = row[ClassSchedules.id].toString(),
                        className = row[ClassSchedules.name],
                        dayOfWeek = dayOfWeek,
                        date = formatUpcomingDate(dayOfWeek),
                        startTime = row[ClassSchedules.startTime],
                        coachName = row[Users.name] ?: "Coach",
                        hasWorkout = workout != null
                    )
                }

            MyClassesResponse(
                today = todayClasses,
                thisWeek = upcomingClasses.take(3),
                upcoming = upcomingClasses
            )

        } catch (e: Exception) {
            println("Error getting my classes: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getAvailableClasses(userId: String, dayOfWeek: String? = null): AvailableClassesResponse? = transaction {
        try {
            val userUuid = UUID.fromString(userId)

            // Obtener athleteId y boxId
            val athlete = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]
            val boxId = athlete[Athletes.boxId] ?: return@transaction null

            // Obtener todas las clases del box
            var query = (ClassSchedules innerJoin Coaches innerJoin Users)
                .selectAll()
                .where {
                    (ClassSchedules.boxId eq boxId) and
                            (Coaches.id eq ClassSchedules.coachId) and
                            (Users.id eq Coaches.userId)
                }


            if (dayOfWeek != null) {
                query = query.andWhere { ClassSchedules.dayOfWeek eq dayOfWeek.uppercase() }
            }

            val classes = query.map { row ->
                val classId = row[ClassSchedules.id]

                // Verificar si el atleta está inscrito
                val isEnrolled = ClassEnrollments
                    .selectAll()
                    .where {
                        (ClassEnrollments.classId eq classId) and
                                (ClassEnrollments.athleteId eq athleteId)
                    }
                    .count() > 0

                // Obtener workout si existe
                val workoutId = row[ClassSchedules.workoutId]
                val workoutTitle = if (workoutId != null) {
                    Workouts
                        .selectAll()
                        .where { Workouts.id eq workoutId }
                        .singleOrNull()
                        ?.get(Workouts.title)
                } else null

                val currentEnrollment = row[ClassSchedules.currentEnrollment]
                val maxCapacity = row[ClassSchedules.maxCapacity]

                AvailableClassDTO(
                    id = classId.toString(),
                    name = row[ClassSchedules.name],
                    dayOfWeek = row[ClassSchedules.dayOfWeek],
                    startTime = row[ClassSchedules.startTime],
                    endTime = row[ClassSchedules.endTime],
                    coachName = row[Users.name] ?: "Coach",
                    maxCapacity = maxCapacity,
                    currentEnrollment = currentEnrollment,
                    spotsLeft = maxCapacity - currentEnrollment,
                    isEnrolled = isEnrolled,
                    workoutId = workoutId?.toString(),
                    workoutTitle = workoutTitle,
                    level = row[ClassSchedules.level]
                )
            }

            val dayOrder = mapOf(
                "MONDAY" to 1,
                "TUESDAY" to 2,
                "WEDNESDAY" to 3,
                "THURSDAY" to 4,
                "FRIDAY" to 5,
                "SATURDAY" to 6,
                "SUNDAY" to 7
            )

            val sortedClasses = classes.sortedWith(
                compareBy(
                    { dayOrder[it.dayOfWeek] ?: 8 },  // Order por día
                    { parseTime(it.startTime) }  // Por hora
                )
            )

            AvailableClassesResponse(classes = sortedClasses)

        } catch (e: Exception) {
            println("Error getting available classes: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getClassDetails(classId: String, userId: String): ClassDetailsDTO? = transaction {
        try {
            val classUuid = UUID.fromString(classId)
            val userUuid = UUID.fromString(userId)

            // Obtener información de la clase
            val classRow = ClassSchedules
                .selectAll()
                .where { ClassSchedules.id eq classUuid }
                .singleOrNull() ?: return@transaction null

            val coachId = classRow[ClassSchedules.coachId]
            val workoutId = classRow[ClassSchedules.workoutId]

            // Información del coach
            val coachInfo = (Coaches innerJoin Users)
                .selectAll()
                .where { Coaches.id eq coachId }
                .singleOrNull()
                ?.let {
                    val specialtiesJson = it[Coaches.specialties]
                    val specialties = try {
                        Json.decodeFromString<List<String>>(specialtiesJson)
                    } catch (_: Exception) {
                        listOf("Coach")
                    }
                    CoachInfoDTO(
                        id = it[Coaches.id].toString(),
                        name = it[Users.name] ?: "Coach",
                        specialty = specialties.firstOrNull() ?: "CrossFit Coach",
                        icon = "👨‍🏫"
                    )
                } ?: return@transaction null

            // Información del workout (si existe)
            val workoutDetails = workoutId?.let { wId ->
                val workout = Workouts
                    .selectAll()
                    .where { Workouts.id eq wId }
                    .singleOrNull() ?: return@let null

                val exercises = Exercises
                    .selectAll()
                    .where { Exercises.workoutId eq wId }
                    .map { ex ->
                        ExerciseDTO(
                            id = ex[Exercises.id].toString(),
                            name = ex[Exercises.name],
                            sets = ex[Exercises.sets],
                            reps = ex[Exercises.reps],
                            weight = ex[Exercises.weight]
                        )
                    }

                WorkoutDetailsDTO(
                    id = workout[Workouts.id].toString(),
                    title = workout[Workouts.title],
                    description = workout[Workouts.description],
                    duration = workout[Workouts.duration],
                    difficulty = workout[Workouts.difficulty],
                    exercises = exercises
                )
            }

            // Atletas inscritos
            val enrolledAthletes = (ClassEnrollments innerJoin Athletes innerJoin Users)
                .selectAll()
                .where { ClassEnrollments.classId eq classUuid }
                .map { row ->
                    val athleteId = row[Athletes.id]

                    // Verificar si completó el workout (si existe)
                    val hasCompleted = workoutId?.let { wId ->
                        WorkoutLogs
                            .selectAll()
                            .where {
                                (WorkoutLogs.athleteId eq athleteId) and
                                        (WorkoutLogs.workoutId eq wId) and
                                        (WorkoutLogs.completedAt greaterEq LocalDate.now().atStartOfDay())
                            }
                            .count() > 0
                    } ?: false

                    EnrolledAthleteDTO(
                        id = athleteId.toString(),
                        name = row[Users.name] ?: "Atleta",
                        hasCompletedWorkout = hasCompleted
                    )
                }

            // Verificar si el usuario actual está inscrito
            val currentAthleteId = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull()?.get(Athletes.id)

            val isEnrolled = currentAthleteId?.let { athleteId ->
                ClassEnrollments
                    .selectAll()
                    .where {
                        (ClassEnrollments.classId eq classUuid) and
                                (ClassEnrollments.athleteId eq athleteId)
                    }
                    .count() > 0
            } ?: false

            val maxCapacity = classRow[ClassSchedules.maxCapacity]
            val currentEnrollment = enrolledAthletes.size

            ClassDetailsDTO(
                classId = classUuid.toString(),
                className = classRow[ClassSchedules.name],
                description = classRow[ClassSchedules.description] ?: "",
                dayOfWeek = classRow[ClassSchedules.dayOfWeek],
                startTime = classRow[ClassSchedules.startTime],
                endTime = classRow[ClassSchedules.endTime],
                level = classRow[ClassSchedules.level],
                maxCapacity = maxCapacity,
                currentEnrollment = currentEnrollment,
                spotsLeft = maxCapacity - currentEnrollment,
                coach = coachInfo,
                workout = workoutDetails,
                enrolledAthletes = enrolledAthletes,
                isEnrolled = isEnrolled
            )

        } catch (e: Exception) {
            println("Error getting class details: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun editClass(
        classId: String,
        userId: String,
        name: String,
        description: String,
        dayOfWeek: String,
        startTime: String,
        endTime: String,
        level: String,
        maxCapacity: Int,
        coachId: String,
        workoutId: String?
    ): EditClassResponse? = transaction {
        try {
            val classUuid = UUID.fromString(classId)
            val userUuid = UUID.fromString(userId)

            val box = Boxes
                .selectAll()
                .where { Boxes.ownerId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]

            // Verificar que la clase pertenece al box del usuario
            val classSchedule = ClassSchedules
                .selectAll()
                .where {
                    (ClassSchedules.id eq classUuid) and
                            (ClassSchedules.boxId eq boxId)
                }
                .singleOrNull() ?: return@transaction null

            val currentEnrollment = classSchedule[ClassSchedules.currentEnrollment]

            if (maxCapacity < currentEnrollment) {
                return@transaction null
            }

            // Verificar que el coach existe y pertenece al box
            val coachUuid = UUID.fromString(coachId)
            val coachExists = Coaches
                .selectAll()
                .where {
                    (Coaches.id eq coachUuid) and
                            (Coaches.boxId eq boxId)
                }
                .count() > 0

            if (!coachExists) {
                return@transaction null
            }

            // Verificar workout
            var workoutExists = true
            if (!workoutId.isNullOrEmpty()) {
                val workoutUuid = UUID.fromString(workoutId)
                workoutExists = Workouts
                    .selectAll()
                    .where { Workouts.id eq workoutUuid }
                    .count() > 0
            }

            if (!workoutExists) {
                return@transaction null
            }

            // Actualizar clase
            ClassSchedules.update({ ClassSchedules.id eq classUuid }) {
                it[ClassSchedules.name] = name
                it[ClassSchedules.dayOfWeek] = dayOfWeek
                it[ClassSchedules.startTime] = startTime
                it[ClassSchedules.endTime] = endTime
                it[ClassSchedules.level] = level
                it[ClassSchedules.maxCapacity] = maxCapacity
                it[ClassSchedules.coachId] = coachUuid
                if (!workoutId.isNullOrEmpty()) {
                    it[ClassSchedules.workoutId] = UUID.fromString(workoutId)
                } else {
                    it[ClassSchedules.workoutId] = null
                }
            }

            // Obtener info del coach
            val coach = Coaches
                .innerJoin(Users)
                .selectAll()
                .where { Coaches.id eq coachUuid }
                .singleOrNull()

            val coachName = coach?.get(Users.name) ?: "Coach"

            // Obtener info del workout
            val workoutTitle = if (!workoutId.isNullOrEmpty()) {
                Workouts
                    .selectAll()
                    .where { Workouts.id eq UUID.fromString(workoutId) }
                    .singleOrNull()
                    ?.get(Workouts.title)
            } else {
                null
            }

            val spotsLeft = maxCapacity - currentEnrollment

            EditClassResponse(
                classId = classUuid.toString(),
                className = name,
                dayOfWeek = dayOfWeek,
                description = description,
                startTime = startTime,
                endTime = endTime,
                level = level,
                maxCapacity = maxCapacity,
                currentEnrollment = currentEnrollment,
                spotsLeft = spotsLeft,
                coachId = coachUuid.toString(),
                coachName = coachName,
                workoutId = workoutId,
                workoutTitle = workoutTitle
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun formatUpcomingDate(dayOfWeek: String): String {
        val today = LocalDate.now()
        val targetDay = DayOfWeek.valueOf(dayOfWeek)
        return when (val daysUntil = (targetDay.value - today.dayOfWeek.value + 7) % 7) {
            0 -> "Hoy"
            1 -> "Mañana"
            else -> {
                val targetDate = today.plusDays(daysUntil.toLong())
                val locale = Locale.forLanguageTag("es")
                "${targetDay.getDisplayName(TextStyle.FULL, locale)} ${targetDate.dayOfMonth} ${targetDate.month.getDisplayName(TextStyle.SHORT, locale)}"
            }
        }
    }

    private fun parseTime(time: String): Int {
        return try {
            val parts = time.split(":")
            var hours = parts[0].toInt()
            val minutes = parts[1].take(2).toInt()
            val isPM = time.contains("PM", ignoreCase = true)

            if (isPM && hours != 12) hours += 12
            if (!isPM && hours == 12) hours = 0

            hours * 60 + minutes
        } catch (_: Exception) {
            0
        }
    }
}