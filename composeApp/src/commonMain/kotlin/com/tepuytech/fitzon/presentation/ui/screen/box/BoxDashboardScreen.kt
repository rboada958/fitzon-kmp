package com.tepuytech.fitzon.presentation.ui.screen.box

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.boxName
import com.tepuytech.fitzon.data.boxStats
import com.tepuytech.fitzon.data.todayClasses
import com.tepuytech.fitzon.data.topAthletes
import com.tepuytech.fitzon.domain.model.ClassSchedule
import com.tepuytech.fitzon.domain.model.TopAthlete
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.NotificationCenterBox
import org.jetbrains.compose.ui.tooling.preview.Preview

class BoxDashboard : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        BoxDashboardScreen(
            onNotificationClick = { navigator.push(NotificationCenterBox()) },
            onProfileClick = { navigator.push(BoxProfile()) }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxDashboardScreen(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = boxName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${boxStats.activeMembersToday} atletas activos hoy",
                            fontSize = 12.sp,
                            color = greenLight
                        )
                    }
                },
                actions = {
                    IconButton(onClick =  onNotificationClick){
                        Box {
                            Text("🔔", fontSize = 24.sp)
                            Surface(
                                modifier = Modifier
                                    .size(14.dp)
                                    .align(Alignment.TopEnd),
                                shape = CircleShape,
                                color = Color(0xFFFF6B6B)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "3",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 8.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier
                            .clickable {
                                onProfileClick()
                            }
                            .size(40.dp)
                            .clip(CircleShape),
                        color = greenPrimary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🏋️", fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Crear anuncio */ },
                containerColor = greenPrimary,
                contentColor = Color(0xFF081C15)
            ) {
                Text("📢", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Publicar Anuncio",
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 200)
                    )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📅 Clases de Hoy",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            TextButton(onClick = { /* Ver todas */ }) {
                                Text(
                                    "Ver todas",
                                    color = greenLight,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            todayClasses.take(3).forEach { classItem ->
                                ClassCard(
                                    classSchedule = classItem,
                                    cardBackground = cardBackground,
                                    greenPrimary = greenPrimary,
                                    greenLight = greenLight,
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
                        animationSpec = tween(500, delayMillis = 500)
                    )
                ) {
                    Column {
                        Text(
                            text = "⭐ Destacados del Mes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            topAthletes.forEach { athlete ->
                                TopAthleteCard(
                                    athlete = athlete,
                                    cardBackground = cardBackground,
                                    greenLight = greenLight
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
fun ClassCard(
    classSchedule: ClassSchedule,
    cardBackground: Color,
    greenPrimary: Color,
    greenLight: Color,
    textGray: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (classSchedule.isNow) greenPrimary.copy(alpha = 0.2f) else cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Column {
                    Text(
                        text = classSchedule.time,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (classSchedule.isNow) greenPrimary else Color.White
                    )
                    if (classSchedule.isNow) {
                        Text(
                            text = "EN CURSO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenPrimary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = classSchedule.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = classSchedule.coach,
                        fontSize = 13.sp,
                        color = textGray
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${classSchedule.currentCapacity}/${classSchedule.maxCapacity}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (classSchedule.currentCapacity >= classSchedule.maxCapacity)
                        Color(0xFFFF6B6B)
                    else
                        greenLight
                )
                Text(
                    text = "personas",
                    fontSize = 11.sp,
                    color = textGray
                )
            }
        }
    }
}

@Composable
fun TopAthleteCard(
    athlete: TopAthlete,
    cardBackground: Color,
    greenLight: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = athlete.icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = athlete.category,
                    fontSize = 12.sp,
                    color = greenLight,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = athlete.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun BoxDashboardScreenPreview() {
    BoxDashboardScreen()
}