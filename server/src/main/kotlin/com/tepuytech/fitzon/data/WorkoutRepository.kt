package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.models.Athletes
import com.tepuytech.fitzon.models.Boxes
import com.tepuytech.fitzon.models.ClassSchedules
import com.tepuytech.fitzon.models.CompleteWorkoutResponse
import com.tepuytech.fitzon.models.CreateExerciseRequest
import com.tepuytech.fitzon.models.ExerciseData
import com.tepuytech.fitzon.models.ExerciseResponse
import com.tepuytech.fitzon.models.Exercises
import com.tepuytech.fitzon.models.PersonalRecordDetected
import com.tepuytech.fitzon.models.PersonalRecords
import com.tepuytech.fitzon.models.WorkoutCompletionStats
import com.tepuytech.fitzon.models.WorkoutLogResponse
import com.tepuytech.fitzon.models.WorkoutLogs
import com.tepuytech.fitzon.models.WorkoutResponse
import com.tepuytech.fitzon.models.Workouts
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class WorkoutRepository {

    fun createWorkout(
        boxOwnerId: String,
        title: String,
        description: String?,
        date: String,
        dayOfWeek: String,
        duration: Int,
        difficulty: String,
        classId: String?,
        exercises: List<CreateExerciseRequest>
    ): WorkoutResponse? = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)

            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = box[Boxes.id]
            val boxName = box[Boxes.name]

            var className: String? = null
            if (classId != null) {
                val classSchedule = ClassSchedules.selectAll()
                    .where {
                        (ClassSchedules.id eq UUID.fromString(classId)) and
                                (ClassSchedules.boxId eq boxId)
                    }
                    .singleOrNull()

                if (classSchedule != null) {
                    className = classSchedule[ClassSchedules.name]
                }
            }

            val workoutId = UUID.randomUUID()
            Workouts.insert {
                it[Workouts.id] = workoutId
                it[Workouts.boxId] = boxId
                it[Workouts.classId] = classId?.let { id -> UUID.fromString(id) }
                it[Workouts.title] = title
                it[Workouts.description] = description
                it[Workouts.date] = LocalDate.parse(date)
                it[Workouts.dayOfWeek] = dayOfWeek
                it[Workouts.duration] = duration
                it[Workouts.difficulty] = difficulty
            }

            val exerciseResponses = exercises.map { exercise ->
                val exerciseId = UUID.randomUUID()
                Exercises.insert {
                    it[Exercises.id] = exerciseId
                    it[Exercises.workoutId] = workoutId
                    it[Exercises.name] = exercise.name
                    it[Exercises.sets] = exercise.sets
                    it[Exercises.reps] = exercise.reps
                    it[Exercises.weight] = exercise.weight
                    it[Exercises.notes] = exercise.notes
                    it[Exercises.videoUrl] = exercise.videoUrl
                }

                ExerciseResponse(
                    id = exerciseId.toString(),
                    name = exercise.name,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    weight = exercise.weight,
                    notes = exercise.notes,
                    videoUrl = exercise.videoUrl
                )
            }

            val workout = Workouts.selectAll()
                .where { Workouts.id eq workoutId }
                .single()

            WorkoutResponse(
                id = workoutId.toString(),
                boxId = boxId.toString(),
                boxName = boxName,
                classId = classId,
                className = className,
                title = title,
                description = description,
                date = date,
                dayOfWeek = dayOfWeek,
                duration = duration,
                difficulty = difficulty,
                createdAt = workout[Workouts.createdAt].toString(),
                exercises = exerciseResponses
            )
        } catch (e: Exception) {
            println("Error in createWorkout: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getWorkoutById(workoutId: String): WorkoutResponse? = transaction {
        try {
            val uuid = UUID.fromString(workoutId)

            val workout = Workouts.selectAll()
                .where { Workouts.id eq uuid }
                .singleOrNull() ?: return@transaction null

            val boxId = workout[Workouts.boxId]
            val box = Boxes.selectAll()
                .where { Boxes.id eq boxId }
                .single()

            val classId = workout[Workouts.classId]
            val className = classId?.let {
                ClassSchedules.selectAll()
                    .where { ClassSchedules.id eq it }
                    .singleOrNull()
                    ?.get(ClassSchedules.name)
            }

            val exercises = Exercises.selectAll()
                .where { Exercises.workoutId eq uuid }
                .map {
                    ExerciseResponse(
                        id = it[Exercises.id].toString(),
                        name = it[Exercises.name],
                        sets = it[Exercises.sets],
                        reps = it[Exercises.reps],
                        weight = it[Exercises.weight],
                        notes = it[Exercises.notes],
                        videoUrl = it[Exercises.videoUrl]
                    )
                }

            WorkoutResponse(
                id = uuid.toString(),
                boxId = boxId.toString(),
                boxName = box[Boxes.name],
                classId = classId?.toString(),
                className = className,
                title = workout[Workouts.title],
                description = workout[Workouts.description],
                exercises = exercises,
                date = workout[Workouts.date].toString(),
                dayOfWeek = workout[Workouts.dayOfWeek],
                duration = workout[Workouts.duration],
                difficulty = workout[Workouts.difficulty],
                createdAt = workout[Workouts.createdAt].toString()
            )
        } catch (e: Exception) {
            println("Error in getWorkoutById: ${e.message}")
            null
        }
    }

    fun getWorkoutsByBox(boxId: String, limit: Int = 20): List<WorkoutResponse> = transaction {
        try {
            val uuid = UUID.fromString(boxId)

            Workouts.selectAll()
                .where { Workouts.boxId eq uuid }
                .orderBy(Workouts.date to SortOrder.DESC)
                .limit(limit)
                .map { workout ->
                    val workoutId = workout[Workouts.id]

                    val box = Boxes.selectAll()
                        .where { Boxes.id eq uuid }
                        .single()

                    val classId = workout[Workouts.classId]
                    val className = classId?.let {
                        ClassSchedules.selectAll()
                            .where { ClassSchedules.id eq it }
                            .singleOrNull()
                            ?.get(ClassSchedules.name)
                    }

                    val exercises = Exercises.selectAll()
                        .where { Exercises.workoutId eq workoutId }
                        .map {
                            ExerciseResponse(
                                id = it[Exercises.id].toString(),
                                name = it[Exercises.name],
                                sets = it[Exercises.sets],
                                reps = it[Exercises.reps],
                                weight = it[Exercises.weight],
                                notes = it[Exercises.notes],
                                videoUrl = it[Exercises.videoUrl]
                            )
                        }

                    WorkoutResponse(
                        id = workoutId.toString(),
                        boxId = uuid.toString(),
                        boxName = box[Boxes.name],
                        classId = classId?.toString(),
                        className = className,
                        title = workout[Workouts.title],
                        description = workout[Workouts.description],
                        date = workout[Workouts.date].toString(),
                        dayOfWeek = workout[Workouts.dayOfWeek],
                        duration = workout[Workouts.duration],
                        difficulty = workout[Workouts.difficulty],
                        createdAt = workout[Workouts.createdAt].toString(),
                        exercises = exercises,
                    )
                }
        } catch (e: Exception) {
            println("Error in getWorkoutsByBox: ${e.message}")
            emptyList()
        }
    }

    fun deleteWorkout(boxOwnerId: String, workoutId: String): Boolean = transaction {
        try {
            val ownerUuid = UUID.fromString(boxOwnerId)
            val workoutUuid = UUID.fromString(workoutId)

            val box = Boxes.selectAll()
                .where { Boxes.ownerId eq ownerUuid }
                .singleOrNull() ?: return@transaction false

            val boxId = box[Boxes.id]

            Exercises.deleteWhere { Exercises.workoutId eq workoutUuid }

            Workouts.deleteWhere {
                (Workouts.id eq workoutUuid) and (Workouts.boxId eq boxId)
            } > 0
        } catch (_: Exception) {
            false
        }
    }

    fun getWorkoutOfDay(athleteId: String): WorkoutResponse? = transaction {
        try {
            val athleteUuid = UUID.fromString(athleteId)

            // Obtener el athlete y su box
            val athlete = Athletes.selectAll()
                .where { Athletes.userId eq athleteUuid }
                .singleOrNull() ?: return@transaction null

            val boxId = athlete[Athletes.boxId] ?: return@transaction null

            // Buscar workout de hoy para ese box
            val today = LocalDate.now()

            val workout = Workouts.selectAll()
                .where {
                    (Workouts.boxId eq boxId) and
                            (Workouts.date eq today)
                }
                .orderBy(Workouts.createdAt to SortOrder.DESC)
                .limit(1)
                .singleOrNull() ?: return@transaction null

            val workoutId = workout[Workouts.id]

            val box = Boxes.selectAll()
                .where { Boxes.id eq boxId }
                .single()

            val classId = workout[Workouts.classId]
            val className = classId?.let {
                ClassSchedules.selectAll()
                    .where { ClassSchedules.id eq it }
                    .singleOrNull()
                    ?.get(ClassSchedules.name)
            }

            val exercises = Exercises.selectAll()
                .where { Exercises.workoutId eq workoutId }
                .map {
                    ExerciseResponse(
                        id = it[Exercises.id].toString(),
                        name = it[Exercises.name],
                        sets = it[Exercises.sets],
                        reps = it[Exercises.reps],
                        weight = it[Exercises.weight],
                        notes = it[Exercises.notes],
                        videoUrl = it[Exercises.videoUrl]
                    )
                }

            WorkoutResponse(
                id = workoutId.toString(),
                boxId = boxId.toString(),
                boxName = box[Boxes.name],
                classId = classId?.toString(),
                className = className,
                title = workout[Workouts.title],
                description = workout[Workouts.description],
                exercises = exercises,
                date = workout[Workouts.date].toString(),
                dayOfWeek = workout[Workouts.dayOfWeek],
                duration = workout[Workouts.duration],
                difficulty = workout[Workouts.difficulty],
                createdAt = workout[Workouts.createdAt].toString()
            )
        } catch (e: Exception) {
            println("Error in getWorkoutOfDay: ${e.message}")
            null
        }
    }

    fun completeWorkout(
        userId: String,
        workoutId: String,
        caloriesBurned: Int,
        durationMinutes: Int,
        notes: String
    ): CompleteWorkoutResponse? = transaction {
        try {
            val userUuid = UUID.fromString(userId)
            val workoutUuid = UUID.fromString(workoutId)

            val athlete = Athletes
                .selectAll()
                .where { Athletes.userId eq userUuid }
                .singleOrNull() ?: return@transaction null

            val athleteId = athlete[Athletes.id]

            val workoutLogId = UUID.randomUUID()

            WorkoutLogs.insert {
                it[WorkoutLogs.id] = workoutLogId
                it[WorkoutLogs.athleteId] = athleteId
                it[WorkoutLogs.workoutId] = workoutUuid
                it[WorkoutLogs.completedAt] = LocalDateTime.now()
                it[WorkoutLogs.caloriesBurned] = caloriesBurned
                it[WorkoutLogs.durationMinutes] = durationMinutes
                it[WorkoutLogs.notes] = notes
            }

            val exercises = Exercises
                .selectAll()
                .where { Exercises.workoutId eq workoutUuid }
                .map { row ->
                    ExerciseData(
                        name = row[Exercises.name],
                        sets = row[Exercises.sets],
                        reps = row[Exercises.reps]
                    )
                }

            val detectedPRs = detectPersonalRecords(athleteId, exercises)

            detectedPRs.filter { it.isNewRecord }.forEach { pr ->
                PersonalRecords.insert {
                    it[PersonalRecords.id] = UUID.randomUUID()
                    it[PersonalRecords.athleteId] = athleteId
                    it[exerciseName] = pr.exerciseName
                    it[value] = pr.newBest.toString()
                    it[unit] = "reps"
                    it[achievedAt] = LocalDateTime.now()
                }
            }

            CompleteWorkoutResponse(
                message = if (detectedPRs.isNotEmpty()) {
                    "¡Workout completado! ${detectedPRs.size} nuevo(s) PR(s) detectado(s) 🎉"
                } else {
                    "Workout completado exitosamente"
                },
                workoutLog = WorkoutLogResponse(
                    id = workoutLogId.toString(),
                    workoutId = workoutId,
                    athleteId = athleteId.toString(),
                    completedAt = LocalDateTime.now().toString(),
                    caloriesBurned = caloriesBurned,
                    durationMinutes = durationMinutes,
                    notes = notes
                ),
                personalRecords = detectedPRs,
                stats = WorkoutCompletionStats(
                    totalPRsToday = detectedPRs.count { it.isNewRecord },
                    caloriesBurned = caloriesBurned,
                    durationMinutes = durationMinutes
                )
            )

        } catch (e: Exception) {
            println("Error completing workout: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun detectPersonalRecords(
        athleteId: UUID,
        exercises: List<ExerciseData>
    ): List<PersonalRecordDetected> {
        return exercises.mapNotNull { exercise ->
            try {
                val currentTotal = exercise.sets * exercise.reps

                val previousBestRow = PersonalRecords
                    .selectAll()
                    .where {
                        (PersonalRecords.athleteId eq athleteId) and
                                (PersonalRecords.exerciseName eq exercise.name)
                    }
                    .orderBy(PersonalRecords.achievedAt to SortOrder.DESC)  // Más reciente primero
                    .firstOrNull()

                val previousBest = previousBestRow?.let { row ->
                    try {
                        row[PersonalRecords.value].toIntOrNull()
                    } catch (_: Exception) {
                        null
                    }
                }

                val isNewPR = previousBest == null || currentTotal > previousBest

                if (isNewPR) {
                    PersonalRecordDetected(
                        exerciseName = exercise.name,
                        previousBest = previousBest,
                        newBest = currentTotal,
                        improvement = if (previousBest != null) {
                            val diff = currentTotal - previousBest
                            "+$diff reps"
                        } else {
                            "First record!"
                        },
                        isNewRecord = true
                    )
                } else {
                    null
                }

            } catch (e: Exception) {
                println("Error detecting PR for ${exercise.name}: ${e.message}")
                null
            }
        }
    }
}