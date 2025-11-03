package com.tepuytech.fitzon.presentation.ui.screen.athlete

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.Leaderboard
import com.tepuytech.fitzon.domain.model.PersonalRecord
import com.tepuytech.fitzon.domain.model.athletes.AthleteDashboardResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import com.tepuytech.fitzon.presentation.state.AthleteUiState
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.NotificationCenter
import com.tepuytech.fitzon.presentation.viewmodel.AthleteViewModel
import com.tepuytech.fitzon.presentation.viewmodel.WorkoutViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class AthleteDashboard : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AthleteViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val workoutViewModel = getScreenModel<WorkoutViewModel>()
        val workoutUiState by workoutViewModel.uiState.collectAsState()


        LaunchedEffect(Unit) {
            workoutViewModel.workoutOfTheDay()
        }

        LaunchedEffect(workoutUiState) {
            when (workoutUiState) {
                is WorkoutUiState.Success,
                is WorkoutUiState.Empty -> {
                    viewModel.athleteDashboard()
                }
                else -> {}
            }
        }

        if (uiState is AthleteUiState.Loading || workoutUiState is WorkoutUiState.Loading) {
            AthleteDashboardShimmer()
            return
        }

        if (workoutUiState is WorkoutUiState.Error) {
            val error = workoutUiState as WorkoutUiState.Error
            Box(
                Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${error.message}", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { workoutViewModel.workoutOfTheDay() }) {
                        Text("Reintentar")
                    }
                }
            }
            return
        }

        if (uiState is AthleteUiState.Error) {
            val error = uiState as AthleteUiState.Error
            Box(
                Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${error.message}", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.athleteDashboard() }) {
                        Text("Reintentar")
                    }
                }
            }
            return
        }

        if (uiState is AthleteUiState.Success) {
            val athleteState = uiState as AthleteUiState.Success

            val workout = when (val state = workoutUiState) {
                is WorkoutUiState.Success -> state.workoutResponse
                else -> null
            }

            AthleteDashboardScreen(
                dashboard = athleteState.dashboard,
                workout = workout,
                onNotificationClick = { navigator.push(NotificationCenter()) },
                onProfileClick = { navigator.push(AthleteProfile()) },
                onWorkoutClick = { navigator.push(WorkoutOfTheDay(workout)) },
                onPersonalRecordsClick = {},
                onLeaderboardClick = {}
            )
        } else {
            AthleteDashboardShimmer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteDashboardScreen(
    dashboard: AthleteDashboardResponse = AthleteDashboardResponse(),
    workout: WorkoutResponse? = null,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPersonalRecordsClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {}
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onNotificationClick) {
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
                            Text("👤", fontSize = 24.sp)
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
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Column {
                        dashboard.userName?.let {
                            val name = it.split(" ").first()
                            Text(
                                text = "¡Hola, $name! 💪",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        dashboard.streakDays.let { streakDays ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (streakDays != null) {
                                    if (streakDays > 0) {
                                        Text(
                                            text = "🔥",
                                            fontSize = 24.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        val label = if (streakDays == 1) "día consecutivo" else "dias consecutivos"
                                        Text(
                                            text = "$streakDays $label entrenando",
                                            fontSize = 16.sp,
                                            color = greenLight,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 100)
                    )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = greenPrimary
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            if (workout?.message.isNullOrEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "WORKOUT DEL DÍA",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF081C15).copy(alpha = 0.7f),
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = workout?.title ?: "No asignado",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF081C15)
                                        )
                                    }

                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF081C15).copy(alpha = 0.2f)
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Text("🏋️", fontSize = 28.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = buildString {
                                        append(workout?.exercises?.size?.toString() ?: "0")
                                        append(" ejercicios • ")
                                        append(workout?.duration?.let { "$it min" } ?: "No asignado")
                                        append(" • ")
                                        append(workout?.difficulty ?: "No asignado")
                                    },
                                    fontSize = 14.sp,
                                    color = Color(0xFF081C15).copy(alpha = 0.8f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { onWorkoutClick() },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF081C15)
                                    )
                                ) {
                                    Text(
                                        "Empezar Ahora",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = greenLight,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }  else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🏋️‍♂️",
                                        fontSize = 42.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = workout.message ?: "No hay un workout disponible por ahora",
                                        fontSize = 16.sp,
                                        color = Color(0xFF081C15).copy(alpha = 0.8f),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { onWorkoutClick() },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF081C15))
                                    ) {
                                        Text(
                                            "Ver otros entrenamientos",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = greenLight,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                dashboard.workoutStats?.let {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInVertically(
                            animationSpec = tween(500, delayMillis = 200)
                        )
                    ) {
                        Column {
                            Text(
                                text = "Esta Semana",
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

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Entrenamientos",
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "${it.completedThisWeek}/${it.totalWeekGoal}",
                                            fontSize = 16.sp,
                                            color = greenLight,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    LinearProgressIndicator(
                                        progress = { it.completedThisWeek.toFloat() / it.totalWeekGoal },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp),
                                        color = greenPrimary,
                                        trackColor = Color(0xFF1B4332),
                                        strokeCap = StrokeCap.Round
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        StatItem(
                                            icon = "🔥",
                                            value = "${it.caloriesBurned}",
                                            label = "Calorías",
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        StatItem(
                                            icon = "⏱️",
                                            value = "${it.totalMinutes}",
                                            label = "Minutos",
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                dashboard.personalRecords?.let { personalRecords ->
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInVertically(
                            animationSpec = tween(500, delayMillis = 300)
                        )
                    ) {
                        if (personalRecords.isNotEmpty()) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Records Personales",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    TextButton(onClick = { onPersonalRecordsClick() }) {
                                        Text(
                                            "Ver todos",
                                            color = greenLight,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {


                                    personalRecords.forEach { record ->
                                        PersonalRecordCard(
                                            record = record,
                                            cardBackground = cardBackground,
                                            greenPrimary = greenPrimary,
                                            textGray = textGray
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 400)
                    )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🏆 Ranking del BOX",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            TextButton(onClick = { onLeaderboardClick() }) {
                                Text(
                                    "Ver más",
                                    color = greenLight,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        dashboard.leaderboard?.let { leaderboard ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = cardBackground
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    leaderboard.forEach { entry ->
                                        LeaderboardItem(
                                            entry = entry,
                                            greenPrimary = greenPrimary,
                                            greenLight = greenLight,
                                            textGray = textGray
                                        )

                                        if (entry != leaderboard.last()) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                color = Color(0xFF1B4332)
                                            )
                                        }
                                    }
                                }
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
fun StatItem(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFFB7B7B7)
        )
    }
}

@Composable
fun PersonalRecordCard(
    record: PersonalRecord,
    cardBackground: Color,
    greenPrimary: Color,
    textGray: Color
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = record.exercise,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    if (record.isNew) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = greenPrimary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "NUEVO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = record.date,
                    fontSize = 12.sp,
                    color = textGray
                )
            }

            Text(
                text = record.value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = greenPrimary
            )
        }
    }
}

@Composable
fun LeaderboardItem(
    entry: Leaderboard,
    greenPrimary: Color,
    greenLight: Color,
    textGray: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (entry.position <= 3) {
                    Text(
                        text = when(entry.position) {
                            1 -> "🥇"
                            2 -> "🥈"
                            3 -> "🥉"
                            else -> ""
                        },
                        fontSize = 24.sp
                    )
                } else {
                    Text(
                        text = "${entry.position}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = if (entry.isCurrentUser) greenPrimary else Color(0xFF1B4332)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = entry.athleteName + if (entry.isCurrentUser) " (Tú)" else "",
                fontSize = 15.sp,
                fontWeight = if (entry.isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                color = if (entry.isCurrentUser) greenLight else Color.White
            )
        }

        Text(
            text = "${entry.points} pts",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (entry.isCurrentUser) greenPrimary else textGray
        )
    }
}

@Preview
@Composable
fun AthleteDashboardScreenPreview() {
    AthleteDashboardScreen()
}