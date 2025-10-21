package com.tepuytech.fitzon.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.notifications
import com.tepuytech.fitzon.domain.enums.NotificationType
import com.tepuytech.fitzon.domain.model.Notification
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.FitzonTopAppBar
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class NotificationCenter : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        NotificationCenterScreen(
            onBackClick = { navigator.pop() }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterScreen(
    onBackClick: () -> Unit = {}
) {

    var notifications by remember {
        mutableStateOf(
            notifications
        )
    }

    var selectedFilter by remember { mutableStateOf<NotificationType?>(null) }
    var showMarkAllRead by remember { mutableStateOf(false) }

    val filteredNotifications = if (selectedFilter == null) {
        notifications
    } else {
        notifications.filter { it.type == selectedFilter }
    }

    val unreadCount = notifications.count { !it.isRead }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            FitzonTopAppBar(
                title = "Notificaciones",
                onBackClick = onBackClick,
                platform = platform
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
                        "🏋️" to NotificationType.WOD,
                        "✅" to NotificationType.RESERVATION,
                        "💚" to NotificationType.PAYMENT,
                        "🏆" to NotificationType.ACHIEVEMENT,
                        "💬" to NotificationType.MESSAGE
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
                                "No hay notificaciones en este filtro",
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
                            NotificationCard(
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
fun NotificationCard(
    notification: Notification,
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
            // Indicador de no leído
            if (!notification.isRead) {
                Surface(
                    modifier = Modifier
                        .size(8.dp),
                    shape = CircleShape,
                    color = greenPrimary
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = when (notification.type) {
                    NotificationType.WOD -> greenPrimary.copy(alpha = 0.2f)
                    NotificationType.RESERVATION -> greenPrimary.copy(alpha = 0.2f)
                    NotificationType.PAYMENT -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                    NotificationType.ACHIEVEMENT -> Color(0xFF74C69D).copy(alpha = 0.2f)
                    NotificationType.COACH -> greenPrimary.copy(alpha = 0.2f)
                    NotificationType.CLASS -> greenPrimary.copy(alpha = 0.2f)
                    NotificationType.SYSTEM -> Color(0xFF5A5A5A).copy(alpha = 0.3f)
                    NotificationType.MESSAGE -> Color(0xFF52B788).copy(alpha = 0.2f)
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
                    Text(
                        text = notification.timestamp,
                        fontSize = 11.sp,
                        color = textGray
                    )

                    if (notification.actionLabel != null) {
                        Surface(
                            modifier = Modifier
                                .background(
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
fun NotificationCenterScreenPreview() {
    NotificationCenterScreen()
}