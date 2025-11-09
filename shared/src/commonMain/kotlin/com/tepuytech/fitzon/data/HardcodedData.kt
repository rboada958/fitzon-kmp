package com.tepuytech.fitzon.data

import com.tepuytech.fitzon.domain.enums.BoxNotificationType
import com.tepuytech.fitzon.domain.enums.NotificationPriority
import com.tepuytech.fitzon.domain.enums.NotificationType
import com.tepuytech.fitzon.domain.model.BoxNotification
import com.tepuytech.fitzon.domain.model.Notification

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