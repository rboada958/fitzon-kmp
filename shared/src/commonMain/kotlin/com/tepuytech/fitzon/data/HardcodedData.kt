package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.domain.enums.BoxNotificationType
import com.tepuytech.fitzon.domain.enums.CoachStatus
import com.tepuytech.fitzon.domain.enums.NotificationPriority
import com.tepuytech.fitzon.domain.enums.NotificationType
import com.tepuytech.fitzon.domain.model.BoxNotification
import com.tepuytech.fitzon.domain.model.Coach
import com.tepuytech.fitzon.domain.model.DayWorkouts
import com.tepuytech.fitzon.domain.model.Notification
import com.tepuytech.fitzon.domain.model.WorkoutItem
import com.tepuytech.fitzon.domain.model.WorkoutOption

/*ManageCoachesScreen*/

val coaches = listOf(
    Coach(
        1,
        "María García",
        "maria.garcia@email.com",
        "+52 123 456 7890",
        listOf("CrossFit", "Olympic Lifting", "Strength"),
        listOf("CrossFit Level 2", "USAW Level 1"),
        CoachStatus.ACTIVE,
        "Ene 2023",
        12,
        4.9f,
        45,
        "8 años"
    ),
    Coach(
        2,
        "Carlos López",
        "carlos.lopez@email.com",
        "+52 123 456 7891",
        listOf("Olympic Lifting", "Strength", "Mobility"),
        listOf("USAW Level 2", "CrossFit Level 1"),
        CoachStatus.ACTIVE,
        "Mar 2023",
        10,
        4.8f,
        38,
        "6 años"
    ),
    Coach(
        3,
        "Ana Martínez",
        "ana.martinez@email.com",
        "+52 123 456 7892",
        listOf("Gymnastics", "CrossFit", "Conditioning"),
        listOf("CrossFit Level 1", "Gymnastics Cert"),
        CoachStatus.ACTIVE,
        "May 2023",
        8,
        4.7f,
        32,
        "5 años"
    ),
    Coach(
        4,
        "Pedro Silva",
        "pedro.silva@email.com",
        "+52 123 456 7893",
        listOf("CrossFit", "Conditioning"),
        listOf("CrossFit Level 1"),
        CoachStatus.ACTIVE,
        "Jun 2023",
        6,
        4.6f,
        25,
        "3 años"
    ),
    Coach(
        5,
        "Laura Torres",
        "laura.torres@email.com",
        "+52 123 456 7894",
        listOf("Nutrition", "Mobility", "CrossFit"),
        listOf("Nutrition Coach", "CrossFit Level 1"),
        CoachStatus.ACTIVE,
        "Jul 2023",
        4,
        4.9f,
        18,
        "4 años"
    ),
    Coach(
        6,
        "Diego Ramírez",
        "diego.ramirez@email.com",
        "+52 123 456 7895",
        listOf("CrossFit", "Olympic Lifting"),
        listOf("CrossFit Level 1"),
        CoachStatus.ON_LEAVE,
        "Aug 2023",
        0,
        4.5f,
        20,
        "2 años"
    ),
    Coach(
        7,
        "Sofia Ruiz",
        "sofia.ruiz@email.com",
        "+52 123 456 7896",
        listOf("Yoga", "Mobility", "Flexibility"),
        listOf("Yoga Certification"),
        CoachStatus.INACTIVE,
        "Sep 2023",
        0,
        4.4f,
        15,
        "1 año"
    )
)

/*RegisterScreen*/

val availableBoxes = listOf(
    "Soldier Box",
    "Iron Box",
    "Beast Mode CrossFit",
    "Elite Athletics",
    "Warrior Box",
)

/*WeeklyWorkoutsScreen*/

val dayWorkouts = listOf(
    DayWorkouts(
        "Monday",
        listOf(
            WorkoutItem(1, "Upper Body", "Strength Training", "🏋️"),
            WorkoutItem(2, "Running", "Cardio", "🏃")
        )
    ),
    DayWorkouts(
        "Tuesday",
        listOf(
            WorkoutItem(3, "Lower Body", "Strength Training", "🏋️"),
            WorkoutItem(4, "Yoga", "Flexibility", "🧘")
        )
    ),
    DayWorkouts(
        "Wednesday",
        listOf(
            WorkoutItem(5, "Core", "Strength Training", "🏋️"),
            WorkoutItem(6, "Swimming", "Cardio", "🏊")
        )
    ),
    DayWorkouts("Thursday", emptyList()),
    DayWorkouts("Friday", emptyList()),
    DayWorkouts("Saturday", emptyList()),
    DayWorkouts("Sunday", emptyList())
)

/*CreateClassScreen*/

val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
val times = listOf("06:00 AM", "09:00 AM", "12:00 PM", "03:00 PM", "05:00 PM", "07:00 PM")
val workoutsByDay = mapOf(
    "Lunes" to listOf(
        WorkoutOption(1, "Full Body Blast", "CrossFit", "Workout completo de cuerpo", 5),
        WorkoutOption(2, "Upper Body Power", "Strength", "Enfoque en tren superior", 4),
        WorkoutOption(3, "Cardio Endurance", "Conditioning", "Resistencia cardiovascular", 6)
    ),
    "Martes" to listOf(
        WorkoutOption(4, "Olympic Lifting", "Olympic", "Levantamiento olímpico técnico", 3),
        WorkoutOption(5, "Lower Body", "Strength", "Piernas intensas", 5),
        WorkoutOption(6, "Gymnastics Skills", "Gymnastics", "Movimientos avanzados", 4)
    ),
    "Miércoles" to listOf(
        WorkoutOption(7, "Mobility & Yoga", "Flexibility", "Recuperación y flexibilidad", 2),
        WorkoutOption(8, "Core Strength", "Strength", "Entrenamiento de core", 4),
        WorkoutOption(9, "High Intensity", "Conditioning", "HIIT intense", 6)
    ),
    "Jueves" to listOf(
        WorkoutOption(10, "Full Body WOD", "CrossFit", "Workout del día", 5),
        WorkoutOption(11, "Partner WOD", "CrossFit", "Entrenamientos en parejas", 4)
    ),
    "Viernes" to listOf(
        WorkoutOption(12, "Competition", "CrossFit", "Entrenamientos de competencia", 5),
        WorkoutOption(13, "Strength Endurance", "Strength", "Fuerza y resistencia", 4)
    ),
    "Sábado" to listOf(
        WorkoutOption(14, "Saturday Showdown", "CrossFit", "Gran cierre de semana", 6),
        WorkoutOption(15, "Open Gym", "CrossFit", "Entrenamiento libre", 7)
    ),
    "Domingo" to listOf(
        WorkoutOption(16, "Recovery Day", "Flexibility", "Descanso activo", 2),
        WorkoutOption(17, "Light WOD", "CrossFit", "Workout ligero", 3)
    )
)

/*NotificationCenter*/

val notifications = listOf(
    Notification(
        1,
        "Nuevo WOD Disponible",
        "Full Body Blast está listo para hoy. ¡No te lo pierdas!",
        NotificationType.WOD,
        "🏋️",
        "Hace 2 horas",
        false,
        "Ver WOD",
        "👁️"
    ),
    Notification(
        2,
        "Confirmación de Reserva",
        "Te has reservado en Morning WOD de mañana a las 6:00 AM",
        NotificationType.RESERVATION,
        "✅",
        "Hace 5 horas",
        false,
        "Ver Clase",
        "📅"
    ),
    Notification(
        3,
        "Pago Recibido",
        "Tu suscripción Premium ha sido renovada exitosamente",
        NotificationType.PAYMENT,
        "💚",
        "Hace 1 día",
        true,
        null,
        null
    ),
    Notification(
        4,
        "¡Logro Desbloqueado!",
        "Completaste tu racha de 7 días. ¡Sigue así! 🔥",
        NotificationType.ACHIEVEMENT,
        "🏆",
        "Hace 2 días",
        true,
        null,
        null
    ),
    Notification(
        5,
        "Nuevo Coach en tu BOX",
        "María García, Certified Coach Level 2, se unió a Soldier Box",
        NotificationType.COACH,
        "👨‍🏫",
        "Hace 3 días",
        true,
        "Conocer Coach",
        "👤"
    ),
    Notification(
        6,
        "Recordatorio de Clase",
        "Tu clase de Gymnastics Skills comienza en 1 hora",
        NotificationType.CLASS,
        "🔔",
        "Hace 1 hora",
        false,
        "Ir a Clase",
        "⏱️"
    ),
    Notification(
        7,
        "Actualización del Sistema",
        "Fitzon ha sido actualizada con nuevas funcionalidades",
        NotificationType.SYSTEM,
        "⚙️",
        "Hace 4 días",
        true,
        "Ver Cambios",
        "📝"
    ),
    Notification(
        8,
        "Nuevo Mensaje",
        "Carlos López te envió un mensaje sobre el WOD de hoy",
        NotificationType.MESSAGE,
        "💬",
        "Hace 30 min",
        false,
        "Ver Mensaje",
        "💬"
    ),
    Notification(
        9,
        "Pago Pendiente",
        "Tu membresía vence en 3 días. Renuévala ahora",
        NotificationType.PAYMENT,
        "⚠️",
        "Hace 5 horas",
        false,
        "Renovar",
        "💳"
    )
)

/*NotificationCenterBoxScreen*/

val notificationsBox = listOf(
    BoxNotification(
        1,
        "⚠️ Pago Pendiente",
        "Juan Pérez debe renovar su membresía. Vence en 3 días.",
        BoxNotificationType.PAYMENT_PENDING,
        "💳",
        "Hace 2 horas",
        false,
        NotificationPriority.HIGH,
        "Recordar",
        "📤"
    ),
    BoxNotification(
        2,
        "Nuevo Miembro Registrado",
        "María López se registró como miembro Premium en Soldier Box",
        BoxNotificationType.NEW_MEMBER,
        "👥",
        "Hace 5 horas",
        false,
        NotificationPriority.MEDIUM,
        "Ver Perfil",
        "👤"
    ),
    BoxNotification(
        3,
        "Pago Recibido",
        "Pago de $500 recibido de Carlos López - Membresía Premium",
        BoxNotificationType.PAYMENT_RECEIVED,
        "💚",
        "Hace 1 día",
        true,
        NotificationPriority.LOW,
        null,
        null
    ),
    BoxNotification(
        4,
        "Cambio de Disponibilidad Coach",
        "Pedro Silva no estará disponible mañana. Morning WOD sin coach asignado.",
        BoxNotificationType.COACH_AVAILABILITY,
        "👨‍🏫",
        "Hace 1 día",
        false,
        NotificationPriority.HIGH,
        "Reasignar",
        "🔄"
    ),
    BoxNotification(
        5,
        "WOD Publicado Exitosamente",
        "Full Body Blast fue publicado. 45 miembros lo verán",
        BoxNotificationType.WOD_PUBLISHED,
        "📋",
        "Hace 2 días",
        true,
        NotificationPriority.MEDIUM,
        null,
        null
    ),
    BoxNotification(
        6,
        "Nueva Reserva en Clase",
        "Diego Ramírez se reservó en Evening Burn. 18/20 lugares",
        BoxNotificationType.CLASS_RESERVATION,
        "✅",
        "Hace 3 horas",
        false,
        NotificationPriority.LOW,
        "Ver Clase",
        "📅"
    ),
    BoxNotification(
        7,
        "Clase Cancelada",
        "Morning WOD de mañana fue cancelada. Notificación enviada a 14 miembros",
        BoxNotificationType.CLASS_CANCELLED,
        "❌",
        "Hace 5 horas",
        false,
        NotificationPriority.URGENT,
        "Ver Detalles",
        "ℹ️"
    ),
    BoxNotification(
        8,
        "Mensaje de Miembro",
        "Sofia Ruiz pregunta sobre opciones de recuperación",
        BoxNotificationType.MEMBER_MESSAGE,
        "💬",
        "Hace 30 min",
        false,
        NotificationPriority.MEDIUM,
        "Responder",
        "💬"
    ),
    BoxNotification(
        9,
        "Reseña Recibida",
        "Laura Torres dejó una reseña de 5 estrellas: 'Excelente ambiente'",
        BoxNotificationType.REVIEW,
        "⭐",
        "Hace 4 horas",
        false,
        NotificationPriority.LOW,
        "Ver Reseña",
        "👁️"
    ),
    BoxNotification(
        10,
        "Reporte Disponible",
        "Reporte mensual de ingresos está listo para descargar",
        BoxNotificationType.REPORT,
        "📊",
        "Hace 1 día",
        true,
        NotificationPriority.MEDIUM,
        "Descargar",
        "📥"
    ),
    BoxNotification(
        11,
        "Actualización del Sistema",
        "Nuevo sistema de reportes disponible. Mejora tu análisis de datos",
        BoxNotificationType.SYSTEM,
        "⚙️",
        "Hace 2 días",
        true,
        NotificationPriority.LOW,
        null,
        null
    )
)