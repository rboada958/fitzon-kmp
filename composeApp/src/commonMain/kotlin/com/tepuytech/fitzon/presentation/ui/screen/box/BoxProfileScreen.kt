package com.tepuytech.fitzon.presentation.ui.screen.box

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.boxProfile
import com.tepuytech.fitzon.data.boxStatsList
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.FitzonSettingItem
import com.tepuytech.fitzon.presentation.ui.composable.FitzonStatCard
import com.tepuytech.fitzon.presentation.ui.composable.FitzonTopAppBar
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.auth.Login
import org.jetbrains.compose.ui.tooling.preview.Preview

class BoxProfile : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        BoxProfileScreen(
            onBackClick = { navigator.pop() },
            onEditClick = {
                navigator.push(EditBoxProfile())
            },
            onManageWorkoutsClick = {
                navigator.push(ManageWorkouts())
            },
            onManageMembersClick = {
                navigator.push(ManageMembers())
            },
            onManageCoachesClick = {
                navigator.push(ManageCoaches())
            },
            onManageClassesClick = {
                navigator.push(ManageClasses())
            },
            onLogoutClick = {
                navigator.replaceAll(Login())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxProfileScreen(
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onManageWorkoutsClick: () -> Unit = {},
    onManageMembersClick: () -> Unit = {},
    onManageCoachesClick: () -> Unit = {},
    onManageClassesClick: () -> Unit = {},
    onManagePaymentsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onSubscriptionClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            FitzonTopAppBar(
                title = "Mi Perfil",
                onBackClick = onBackClick,
                platform = platform,
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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Profile Card
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF1B4332)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Logo
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = CircleShape,
                                color = greenPrimary,
                                border = BorderStroke(4.dp, greenLight)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🏋️", fontSize = 50.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = boxProfile.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⭐", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${boxProfile.rating}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = greenLight
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(${boxProfile.activeMembers} valoraciones)",
                                    fontSize = 14.sp,
                                    color = textGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = greenPrimary.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📅", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Fundado en ${boxProfile.foundedYear}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = greenLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = boxProfile.description,
                                fontSize = 14.sp,
                                color = textGray,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { onEditClick() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                )
                            ) {
                                Text(
                                    "✏️ Editar Perfil del BOX",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF081C15),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Estadísticas
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 100)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        boxStatsList.forEach { (icon, value, label) ->
                            FitzonStatCard(
                                icon = icon,
                                value = value,
                                label = label,
                                cardBackground = cardBackground,
                                modifier = Modifier.weight(1f)
                            )
                            if (boxStatsList.last() != Triple(icon, value, label)) {
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información de Contacto
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 200)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Información de Contacto",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                ContactRow("📧", "Email", boxProfile.email)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                ContactRow("📱", "Teléfono", boxProfile.phone)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                ContactRow("📍", "Dirección", boxProfile.address)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Gestión
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 400)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Gestión del BOX",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                ManagementItem(
                                    icon = "📋",
                                    title = "Gestionar Workouts",
                                    onClick = {
                                        onManageWorkoutsClick()
                                    }
                                )
                                ManagementItem(
                                    icon = "👥",
                                    title = "Gestionar Miembros",
                                    onClick = {
                                        onManageMembersClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                ManagementItem(
                                    icon ="👨‍🏫",
                                    title = "Gestionar Coaches",
                                    onClick = {
                                        onManageCoachesClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                ManagementItem(
                                    icon ="📅",
                                    title = "Horarios y Clases",
                                    onClick = {
                                        onManageClassesClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                ManagementItem(
                                    icon ="💳",
                                    title = "Pagos y Suscripciones",
                                    onClick = {
                                        onManagePaymentsClick()
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Configuración
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 500)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Configuración",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                FitzonSettingItem(
                                    icon = "🔔",
                                    title = "Notificaciones",
                                    onClick = {
                                        onNotificationsClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "🔒",
                                    title = "Privacidad y Seguridad",
                                    onClick = {
                                        onPrivacyClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "💼",
                                    title = "Plan de Suscripción",
                                    onClick = {
                                        onSubscriptionClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "❓",
                                    title = "Ayuda y Soporte",
                                    onClick = {
                                        onHelpClick()
                                    }
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "🚪",
                                    title = "Cerrar Sesión",
                                    onClick = {
                                        onLogoutClick()
                                    },
                                    isDestructive = true
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ContactRow(
    icon: String,
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFFB7B7B7)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}

@Composable
fun ManagementItem(
    icon: String,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Text(
                text = "›",
                fontSize = 24.sp,
                color = Color(0xFFB7B7B7)
            )
        }
    }
}

@Preview
@Composable
fun BoxProfileScreenPreview() {
    BoxProfileScreen()
}

