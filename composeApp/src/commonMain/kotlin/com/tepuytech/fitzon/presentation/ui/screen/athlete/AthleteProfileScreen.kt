package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.Achievement
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.AthleteUiState
import com.tepuytech.fitzon.presentation.state.LogoutUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.FitzonSettingItem
import com.tepuytech.fitzon.presentation.ui.composable.FitzonStatCard
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.NotificationCenter
import com.tepuytech.fitzon.presentation.ui.screen.auth.Login
import com.tepuytech.fitzon.presentation.viewmodel.AthleteViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class AthleteProfile : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AthleteViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val logoutState by viewModel.logoutState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.athleteProfile()
        }

        LaunchedEffect(uiState) {
            when (uiState) {
                is AthleteUiState.Success -> {}
                is AthleteUiState.Error -> {}
                else -> {}
            }
        }

        LaunchedEffect(logoutState) {
            if (logoutState is LogoutUiState.Success) {
                navigator.replaceAll(Login())
            }
        }

        if (uiState is AthleteUiState.Loading) {
            AthleteDashboardShimmer()
            return
        }

        if (uiState is AthleteUiState.ProfileSuccess) {
            val profile = (uiState as AthleteUiState.ProfileSuccess).profile
            val boxId = profile.boxId ?: ""
            AthleteProfileScreen(
                profile = profile,
                onBackClick = {
                    navigator.pop()
                },
                onNotificationClick = {
                    navigator.push(NotificationCenter())
                },
                onBoxProfileClick = {
                    navigator.push(BoxInfoAthlete(boxId))
                },
                onEditProfileClick = {
                    navigator.push(EditAthleteProfile(profile))
                },
                onLogoutClick = {
                    viewModel.logout()
                }
            )
        }
    }
}

@Suppress("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteProfileScreen(
    profile: AthleteProfileResponse = AthleteProfileResponse(),
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onBoxProfileClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
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
                    IconButton(onClick = { onNotificationClick() }) {
                        Text(
                            text = "🔔",
                            fontSize = 24.sp
                        )
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
                            // Avatar
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = CircleShape,
                                color = greenPrimary,
                                border = BorderStroke(4.dp, greenLight)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👤", fontSize = 50.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = profile.name ?: "",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = profile.email ?: "",
                                fontSize = 14.sp,
                                color = textGray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = greenPrimary.copy(alpha = 0.2f),
                                modifier = Modifier.clickable {
                                    onBoxProfileClick()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🏋️", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = profile.boxName  ?: "",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = greenLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Miembro desde ${profile.memberSince}",
                                fontSize = 12.sp,
                                color = textGray
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { onEditProfileClick() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                )
                            ) {
                                Text(
                                    "✏️ Editar Perfil",
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
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        profile.stats?.forEach { (icon, value, label) ->
                            FitzonStatCard(
                                icon = icon,
                                value = value,
                                label = label,
                                cardBackground = cardBackground,
                                modifier = Modifier.weight(1f)
                            )
                            if (profile.stats?.last() != Triple(icon, value, label)) {
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información Personal
                if (profile.weight != null || profile.height != null || profile.age != null)
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
                            text = "Información Personal",
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
                                modifier = Modifier.padding(16.dp)
                            ) {
                                InfoRow("⚖️", "Peso", profile.weight  ?: "")
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                InfoRow("📏", "Altura", profile.height  ?: "")
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                InfoRow("🎂", "Edad", "${profile.age} años")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logros
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 300)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🏆 Logros",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "${profile.achievements?.count { it.isUnlocked }}/${profile.achievements?.size}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenLight
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            profile.achievements?.forEach { achievement ->
                                AchievementCard(
                                    achievement = achievement,
                                    cardBackground = cardBackground,
                                    greenPrimary = greenPrimary,
                                    textGray = textGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                                    onClick = {}
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "🔒",
                                    title = "Privacidad",
                                    onClick = {}
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon = "💳", 
                                    title = "Suscripción",
                                    onClick = {}
                                )
                                HorizontalDivider(color = Color(0xFF1B4332))
                                FitzonSettingItem(
                                    icon ="🚪",
                                    title ="Cerrar Sesión",
                                    onClick = {
                                        onLogoutClick()
                                    },
                                    isDestructive = true
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@Composable
fun InfoRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color(0xFFB7B7B7)
            )
        }

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun AchievementCard(
    achievement: Achievement,
    cardBackground: Color,
    greenPrimary: Color,
    textGray: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = cardBackground,
        border = if (achievement.isUnlocked)
            BorderStroke(2.dp, greenPrimary.copy(alpha = 0.3f))
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = if (achievement.isUnlocked)
                    greenPrimary.copy(alpha = 0.2f)
                else
                    Color(0xFF1B4332)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = achievement.icon,
                        fontSize = 24.sp,
                        modifier = Modifier.then(
                            if (!achievement.isUnlocked)
                                Modifier.alpha(0.3f)
                            else
                                Modifier
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (achievement.isUnlocked) Color.White else textGray
                )
                Text(
                    text = achievement.description,
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            if (achievement.isUnlocked) {
                Text("✓", fontSize = 24.sp, color = greenPrimary)
            } else {
                Text("🔒", fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun AthleteProfileScreenPreview() {
    AthleteProfileScreen()
}