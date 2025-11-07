package com.tepuytech.fitzon.presentation.ui.screen.box

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.DayWorkouts
import com.tepuytech.fitzon.domain.model.WorkoutItem
import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.viewmodel.WorkoutViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class ManageWorkouts(val boxId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WorkoutViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        var dayWorkouts by remember { mutableStateOf(emptyList<DayWorkouts>()) }


        LaunchedEffect(Unit) {
            viewModel.boxWorkout(boxId)
        }

        when (uiState) {
            is WorkoutUiState.Loading -> {
                AthleteDashboardShimmer()
                return
            }
            is WorkoutUiState.SuccessBoxWorkout -> {
                val workoutState = (uiState as WorkoutUiState.SuccessBoxWorkout).boxWorkoutData
                dayWorkouts = groupWorkoutByDay(workoutState)
            }
            is WorkoutUiState.SuccessDeleteWorkout -> {
                val workoutState = (uiState as WorkoutUiState.SuccessDeleteWorkout).message
                println(workoutState)
            }
            is WorkoutUiState.Error -> {
                Text("Error: ${(uiState as WorkoutUiState.Error).message}")
            }

            else -> {}
        }

        ManageWorkoutsScreen(
            dayWorkouts = dayWorkouts,
            onBackClick = {
                navigator.pop()
            },
            onEditWorkout = {
            },
            onDeleteWorkout = { workoutId ->
                viewModel.deleteWorkout(workoutId)
            },
            onCreateWorkoutClick = {
                navigator.push(CreateWorkout())
            }
        )

        if (uiState is WorkoutUiState.LoadingDeleteWorkout) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = greenLight,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageWorkoutsScreen(
    dayWorkouts: List<DayWorkouts> = emptyList(),
    onBackClick: () -> Unit = {},
    onEditWorkout: (String) -> Unit = {},
    onDeleteWorkout: (String) -> Unit = { _ -> },
    onCreateWorkoutClick: () -> Unit = {}
) {
    var weeklyWorkouts by remember {
        mutableStateOf(
            dayWorkouts
        )
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var workoutToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Weekly Workouts",
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
                    IconButton(onClick = { /* Menú de opciones */ }) {
                        Text(
                            text = "⋮",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
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
                onClick = { onCreateWorkoutClick() },
                containerColor = greenPrimary,
                contentColor = Color(0xFF081C15)
            ) {
                Text(
                    text = "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add Workout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
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

                weeklyWorkouts.forEach { dayWorkouts ->
                    DaySection(
                        dayWorkouts = dayWorkouts,
                        cardBackground = cardBackground,
                        textGray = textGray,
                        onEditWorkout = { workoutId ->
                            onEditWorkout(workoutId)
                        },
                        onDeleteWorkout = { day, workoutId ->
                            workoutToDelete = day to workoutId
                            showDeleteDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (showDeleteDialog && workoutToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Eliminar Workout",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar este workout?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        val (day, workoutId) = workoutToDelete!!
                        weeklyWorkouts = weeklyWorkouts.map { dayWorkout ->
                            if (dayWorkout.day == day) {
                                dayWorkout.copy(
                                    workouts = dayWorkout.workouts.filter { it.id != workoutId }
                                )
                            } else {
                                dayWorkout
                            }
                        }
                        showDeleteDialog = false
                        workoutToDelete = null
                        onDeleteWorkout(workoutId)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1B4332),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFB7B7B7)
        )
    }
}

@Composable
fun DaySection(
    dayWorkouts: DayWorkouts,
    cardBackground: Color,
    textGray: Color,
    onEditWorkout: (String) -> Unit,
    onDeleteWorkout: (String, String) -> Unit
) {
    Column {
        Text(
            text = dayWorkouts.day,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (dayWorkouts.workouts.isEmpty()) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBackground.copy(alpha = 0.5f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay workouts programados",
                        fontSize = 14.sp,
                        color = textGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dayWorkouts.workouts.forEach { workout ->
                    WorkoutCard(
                        workout = workout,
                        cardBackground = cardBackground,
                        textGray = textGray,
                        onEdit = { onEditWorkout(workout.id) },
                        onDelete = { onDeleteWorkout(dayWorkouts.day, workout.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: WorkoutItem,
    cardBackground: Color,
    textGray: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1B4332)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = workout.icon,
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Workout Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = workout.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = workout.type,
                    fontSize = 14.sp,
                    color = textGray
                )
            }

            Box {
                IconButton(
                    onClick = { showMenu = !showMenu }
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = Color(0xFF1B4332)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "✏️",
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF1B4332))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✏️ ", fontSize = 18.sp)
                                Text("Editar", color = Color.White)
                            }
                        },
                        onClick = {
                            onEdit()
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📋 ", fontSize = 18.sp)
                                Text("Duplicar", color = Color.White)
                            }
                        },
                        onClick = {
                            // Acción duplicar
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📅 ", fontSize = 18.sp)
                                Text("Mover a otro día", color = Color.White)
                            }
                        },
                        onClick = {
                            // Acción mover
                            showMenu = false
                        }
                    )

                    HorizontalDivider(color = Color(0xFF2D6A4F))

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🗑️ ", fontSize = 18.sp)
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

private fun groupWorkoutByDay(workouts: List<BoxWorkoutResponse>): List<DayWorkouts> {
    val backendDays = listOf(
        "MONDAY", "TUESDAY", "WEDNESDAY",
        "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
    )

    // Mapeo inglés -> español
    val dayTranslations = mapOf(
        "MONDAY" to "Lunes",
        "TUESDAY" to "Martes",
        "WEDNESDAY" to "Miércoles",
        "THURSDAY" to "Jueves",
        "FRIDAY" to "Viernes",
        "SATURDAY" to "Sábado",
        "SUNDAY" to "Domingo"
    )

    val grouped = workouts.groupBy { it.dayOfWeek.uppercase() }

    return backendDays.map { backendDay ->
        val dayWorkouts = grouped[backendDay]?.map {
            WorkoutItem(
                id = it.id,
                name = it.title,
                type = it.difficulty,
                icon = when (it.difficulty.lowercase()) {
                    "strength" -> "🏋️"
                    "cardio" -> "🏃"
                    "flexibility" -> "🧘"
                    else -> "💪"
                }
            )
        } ?: emptyList()

        DayWorkouts(
            day = dayTranslations[backendDay] ?: backendDay,
            workouts = dayWorkouts
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyWorkoutsScreenPreview() {
    ManageWorkoutsScreen()
}