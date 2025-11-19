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