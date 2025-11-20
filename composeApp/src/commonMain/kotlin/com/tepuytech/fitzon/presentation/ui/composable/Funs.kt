package com.tepuytech.fitzon.presentation.ui.composable

fun mapDayToSpanish(day: String): String {
    return when (day) {
        "MONDAY" -> "Lunes"
        "TUESDAY" -> "Martes"
        "WEDNESDAY" -> "Miércoles"
        "THURSDAY" -> "Jueves"
        "FRIDAY" -> "Viernes"
        "SATURDAY" -> "Sábado"
        "SUNDAY" -> "Domingo"
        else -> day
    }
}

fun mapLevelToSpanish(level: String): String {
    return when (level) {
        "BEGINNER" -> "Principiante"
        "INTERMEDIATE" -> "Intermedio"
        "ADVANCED" -> "Avanzado"
        else -> level
    }
}

fun normalizeExerciseName(exerciseName: String): String {
    return exerciseName
        .replace("-", "_")
        .lowercase()
}

fun formatExerciseName(exerciseName: String): String {
    return exerciseName
        .replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}

fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        if (parts.size != 3) return dateString

        val year = parts[0]
        val month = parts[1].toIntOrNull() ?: return dateString
        val day = parts[2].toIntOrNull() ?: return dateString

        val monthName = when (month) {
            1 -> "Ene"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Abr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Ago"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dic"
            else -> return dateString
        }

        "$day $monthName $year"

    } catch (e: Exception) {
        println("Error formatting date: ${e.message}")
        dateString
    }
}

fun parseTime(timeString: String): Int {
    val parts = timeString.split(":")
    var hours = parts[0].toInt()
    val minutes = parts[1].split(" ")[0].toInt()
    val isPM = timeString.contains("PM")

    if (isPM && hours != 12) hours += 12
    if (!isPM && hours == 12) hours = 0

    return hours * 60 + minutes
}