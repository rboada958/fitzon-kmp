package com.tepuytech.fitzon.presentation.ui.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.notificationsBox
import com.tepuytech.fitzon.domain.enums.BoxNotificationType
import com.tepuytech.fitzon.domain.enums.NotificationPriority
import com.tepuytech.fitzon.domain.model.BoxNotification
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class NotificationCenterBox : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        NotificationCenterBoxScreen(
            onBackClick = { navigator.pop() }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterBoxScreen(
    onBackClick: () -> Unit = {}
) {
    var notifications by remember {
        mutableStateOf(
            notificationsBox
        )
    }

    var selectedFilter by remember { mutableStateOf<BoxNotificationType?>(null) }
    var selectedPriority by remember { mutableStateOf<NotificationPriority?>(null) }
    var showMarkAllRead by remember { mutableStateOf(false) }

    val filteredNotifications = notifications.filter { notification ->
        val matchesType = selectedFilter == null || notification.type == selectedFilter
        val matchesPriority = selectedPriority == null || notification.priority == selectedPriority
        matchesType && matchesPriority
    }

    val unreadCount = notifications.count { !it.isRead }
    val urgentCount = notifications.count { it.priority == NotificationPriority.URGENT && !it.isRead }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Notificaciones del BOX",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        if (unreadCount > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$unreadCount sin leer",
                                    fontSize = 12.sp,
                                    color = greenLight
                                )
                                if (urgentCount > 0) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFFF6B6B)
                                    ) {
                                        Text(
                                            text = "$urgentCount urgente",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        if (platform.name.contains("iOS")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (unreadCount > 0) {
                        IconButton(onClick = { showMarkAllRead = true }) {
                            Text("✓", fontSize = 24.sp, color = greenLight)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Filtros horizontales por tipo
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { selectedFilter = null },
                            label = { Text("Todos (${notifications.size})", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = greenPrimary,
                                selectedLabelColor = Color(0xFF081C15),
                                containerColor = cardBackground,
                                labelColor = Color.White
                            )
                        )
                    }

                    items(listOf(
                        "👥" to BoxNotificationType.NEW_MEMBER,
                        "💳" to BoxNotificationType.PAYMENT_RECEIVED,
                        "⚠️" to BoxNotificationType.PAYMENT_PENDING,
                        "👨‍🏫" to BoxNotificationType.COACH_AVAILABILITY,
                        "📋" to BoxNotificationType.WOD_PUBLISHED
                    )) { (icon, type) ->
                        val count = notifications.count { it.type == type }
                        FilterChip(
                            selected = selectedFilter == type,
                            onClick = { selectedFilter = type },
                            label = { Text("$icon $count", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = greenPrimary,
                                selectedLabelColor = Color(0xFF081C15),
                                containerColor = cardBackground,
                                labelColor = Color.White
                            )
                        )
                    }
                }

                // Lista de notificaciones
                if (filteredNotifications.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📭", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Sin notificaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                "No hay notificaciones con estos filtros",
                                fontSize = 14.sp,
                                color = textGray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredNotifications) { notification ->
                            BoxNotificationCard(
                                notification = notification,
                                cardBackground = cardBackground,
                                greenPrimary = greenPrimary,
                                textGray = textGray,
                                onMarkAsRead = {
                                    notifications = notifications.map { n ->
                                        if (n.id == notification.id) n.copy(isRead = true) else n
                                    }
                                },
                                onDelete = {
                                    notifications = notifications.filter { it.id != notification.id }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }

    // Diálogo para marcar todas como leídas
    if (showMarkAllRead) {
        AlertDialog(
            onDismissRequest = { showMarkAllRead = false },
            title = {
                Text(
                    text = "Marcar todas como leídas",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Deseas marcar las $unreadCount notificaciones sin leer como leídas?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        notifications = notifications.map { it.copy(isRead = true) }
                        showMarkAllRead = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = greenPrimary
                    )
                ) {
                    Text("Marcar", color = Color(0xFF081C15))
                }
            },
            dismissButton = {
                TextButton(onClick = { showMarkAllRead = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1B4332),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}

@Composable
fun BoxNotificationCard(
    notification: BoxNotification,
    cardBackground: Color,
    greenPrimary: Color,
    textGray: Color,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardBackground.copy(
            alpha = if (notification.isRead) 0.6f else 1f
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de prioridad y estado
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(12.dp)
            ) {
                if (!notification.isRead) {
                    Surface(
                        modifier = Modifier.size(8.dp),
                        shape = CircleShape,
                        color = greenPrimary
                    ) {}
                }

                if (notification.priority == NotificationPriority.URGENT) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        modifier = Modifier.size(6.dp),
                        shape = CircleShape,
                        color = Color(0xFFFF6B6B)
                    ) {}
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Ícono de notificación
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = when (notification.type) {
                    BoxNotificationType.NEW_MEMBER -> greenPrimary.copy(alpha = 0.2f)
                    BoxNotificationType.PAYMENT_RECEIVED -> Color(0xFF52B788).copy(alpha = 0.2f)
                    BoxNotificationType.PAYMENT_PENDING -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                    BoxNotificationType.COACH_AVAILABILITY -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                    BoxNotificationType.WOD_PUBLISHED -> greenPrimary.copy(alpha = 0.2f)
                    BoxNotificationType.CLASS_RESERVATION -> greenPrimary.copy(alpha = 0.2f)
                    BoxNotificationType.CLASS_CANCELLED -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                    BoxNotificationType.MEMBER_MESSAGE -> greenPrimary.copy(alpha = 0.2f)
                    BoxNotificationType.REPORT -> Color(0xFF5A5A5A).copy(alpha = 0.3f)
                    BoxNotificationType.REVIEW -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                    BoxNotificationType.SYSTEM -> Color(0xFF5A5A5A).copy(alpha = 0.3f)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(notification.icon, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    if (!notification.isRead) {
                        Surface(
                            shape = CircleShape,
                            color = greenPrimary
                        ) {
                            Text(
                                "●",
                                fontSize = 8.sp,
                                color = greenPrimary,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = if (notification.isRead) textGray else Color.White,
                    lineHeight = 18.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = notification.timestamp,
                            fontSize = 11.sp,
                            color = textGray
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Badge de prioridad
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (notification.priority) {
                                NotificationPriority.URGENT -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                                NotificationPriority.HIGH -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                                NotificationPriority.MEDIUM -> greenPrimary.copy(alpha = 0.2f)
                                NotificationPriority.LOW -> Color(0xFF5A5A5A).copy(alpha = 0.3f)
                            }
                        ) {
                            Text(
                                text = notification.priority.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (notification.priority) {
                                    NotificationPriority.URGENT -> Color(0xFFFF6B6B)
                                    NotificationPriority.HIGH -> Color(0xFFFFB84D)
                                    NotificationPriority.MEDIUM -> greenPrimary
                                    NotificationPriority.LOW -> Color(0xFFB7B7B7)
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (notification.actionLabel != null) {
                        Surface(
                            modifier = Modifier.background(
                                color = greenPrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    notification.actionIcon ?: "",
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                notification.actionLabel?.let {
                                    Text(
                                        it,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = greenPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Menú
            Box {
                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("⋮", fontSize = 20.sp, color = textGray)
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF1B4332))
                ) {
                    if (!notification.isRead) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("✓ ", fontSize = 16.sp)
                                    Text("Marcar como leído", color = Color.White)
                                }
                            },
                            onClick = {
                                onMarkAsRead()
                                showMenu = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🗑️ ", fontSize = 16.sp)
                                Text("Eliminar", color = Color(0xFFFF6B6B))
                            }
                        },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationCenterBoxScreenPreview() {
    NotificationCenterBoxScreen()
}