package com.tepuytech.fitzon.presentation.ui.screen.athlete


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
import com.tepuytech.fitzon.domain.model.workout.ExerciseResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
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

class WorkoutOfTheDay(val workoutId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WorkoutViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        var showCompletionDialog by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf("") }

        val workout =
            if (uiState is WorkoutUiState.Success) (uiState as WorkoutUiState.Success).workoutResponse
            else null


        LaunchedEffect(Unit) {
            viewModel.getWorkoutById(workoutId)
        }

        LaunchedEffect(uiState) {
            when (uiState) {
                is WorkoutUiState.SuccessCompleteWorkout -> {
                    message = (uiState as WorkoutUiState.SuccessCompleteWorkout).completeWorkoutResponse.message
                    showCompletionDialog = true
                }
                else -> {}
            }
        }

        when (uiState) {
            is WorkoutUiState.LoadingWorkout -> {
                AthleteDashboardShimmer()
            }

            is WorkoutUiState.Error -> {
                val error = uiState as WorkoutUiState.Error
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(brush = backgroundGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = error.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navigator.pop() },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = greenPrimary,
                                disabledContainerColor = Color(0xFF2D6A4F)
                            )
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }

            is WorkoutUiState.Success,
            is WorkoutUiState.SuccessCompleteWorkout,
            is WorkoutUiState.Loading -> {
                if (workout != null) {
                    WorkoutOfTheDayScreen(
                        workout = workout,
                        onBackClick = { navigator.pop() },
                        onCompletedWodClick = { id, calories, duration, notes ->
                            viewModel.completeWorkout(id, calories, duration, notes)
                        }
                    )

                    // Overlay de loading solo cuando está en Loading
                    if (uiState is WorkoutUiState.Loading) {
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = backgroundGradient),
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

            else -> {}
        }

        if (showCompletionDialog) {
            AlertDialog(
                onDismissRequest = { showCompletionDialog = false },
                title = {
                    Text(
                        text = "🎉 ¡Felicidades!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                text = {
                    Text(
                        text = message,
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showCompletionDialog = false
                            navigator.pop()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        )
                    ) {
                        Text("Aceptar", color = Color(0xFF081C15))
                    }
                },
                containerColor = Color(0xFF1B4332),
                titleContentColor = Color.White,
                textContentColor = textGray
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutOfTheDayScreen(
    workout: WorkoutResponse? = null,
    onBackClick: () -> Unit,
    onCompletedWodClick: (String, Int, Int, String) -> Unit = { _, _, _, _ -> }
) {
    var exercises by remember(workout) {
        mutableStateOf(workout?.exercises ?: emptyList())
    }


    val allCompleted = if (exercises.isNotEmpty()) exercises.all { it.isCompleted } else false

    val buttonScale by animateFloatAsState(
        targetValue = if (allCompleted) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Workout of the Day",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        if (workout != null && !workout.exercises.isNullOrEmpty()) {
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
                    // Header Section
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = workout.title ?: "",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 40.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = workout.description ?: "",
                            fontSize = 16.sp,
                            color = textGray,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress indicator
                        val completedCount = exercises.count { it.isCompleted }
                        val totalCount = exercises.size

                        if (totalCount > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                LinearProgressIndicator(
                                    progress = { completedCount.toFloat() / totalCount },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp),
                                    color = greenPrimary,
                                    trackColor = Color(0xFF2D6A4F),
                                    strokeCap = StrokeCap.Round
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = "$completedCount/$totalCount",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = greenPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Exercise List
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        exercises.forEach { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                cardBackground = cardBackground,
                                greenPrimary = greenPrimary,
                                textGray = textGray,
                                onToggleComplete = { exerciseId ->
                                    exercises = exercises.map {
                                        if (it.id == exerciseId) {
                                            it.copy(isCompleted = !it.isCompleted)
                                        } else {
                                            it
                                        }
                                    }
                                },
                                onEdit = {},
                                onDelete = { exerciseId ->
                                    exercises = exercises.filter { it.id != exerciseId }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }

                // Bottom Button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF081C15).copy(alpha = 0.95f),
                                    Color(0xFF081C15)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = {
                            if (allCompleted && workout.id != null) {
                                val calories = calculateCalories(
                                    workout.duration ?: 0,
                                    workout.difficulty ?: ""
                                )

                                val duration = workout.duration ?: 0
                                val id = workout.id ?: ""

                                val completedCount = exercises.count { it.isCompleted }
                                val totalCount = exercises.size
                                val notes = generateWorkoutNotes(totalCount, completedCount)

                                onCompletedWodClick(id, calories, duration, notes)
                            }
                        },
                        enabled = allCompleted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .scale(buttonScale),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary,
                            disabledContainerColor = Color(0xFF2D6A4F)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (allCompleted) {
                                Text(
                                    text = "✓ ",
                                    fontSize = 20.sp,
                                    color = Color(0xFF081C15)
                                )
                            }
                            Text(
                                text = if (allCompleted) "Marcar como Completado" else "Completa todos los ejercicios",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (allCompleted) Color(0xFF081C15) else textGray
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = workout?.message ?: "No hay workout disponible hoy 💪",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseResponse,
    cardBackground: Color,
    greenPrimary: Color,
    textGray: Color,
    onToggleComplete: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (exercise.isCompleted) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (exercise.isCompleted) 0.6f else 1f,
        animationSpec = tween(300),
        label = "cardAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = cardBackground.copy(alpha = alpha),
        onClick = { onToggleComplete(exercise.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp)
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = if (exercise.isCompleted)
                        greenPrimary.copy(alpha = 0.5f)
                    else
                        greenPrimary
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🏋️",
                            fontSize = 28.sp
                        )
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = exercise.isCompleted,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = greenPrimary.copy(alpha = 0.9f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF081C15)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (exercise.isCompleted)
                        textGray
                    else
                        Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildString {
                        append("${exercise.sets} sets | ")
                        append("${exercise.reps} reps")
                    },
                    fontSize = 14.sp,
                    color = textGray
                )
            }

            // Menu Button
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Text(
                        text = "⋮",
                        fontSize = 24.sp,
                        color = textGray
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF1B4332))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✓ ", fontSize = 18.sp)
                                Text(
                                    if (exercise.isCompleted) "Marcar como pendiente" else "Marcar como completado",
                                    color = Color.White
                                )
                            }
                        },
                        onClick = {
                            onToggleComplete(exercise.id)
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✏️ ", fontSize = 18.sp)
                                Text("Editar ejercicio", color = Color.White)
                            }
                        },
                        onClick = {
                            onEdit(exercise.id)
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
                            onDelete(exercise.id)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

fun calculateCalories(durationMinutes: Int, difficulty: String): Int {
    val caloriesPerMinute = when (difficulty.uppercase()) {
        "BEGINNER", "SCALED" -> 10.0
        "INTERMEDIATE", "RX" -> 12.5
        "ADVANCED", "RX / SCALED" -> 15.0
        else -> 12.0
    }

    return (durationMinutes * caloriesPerMinute).toInt()
}

fun generateWorkoutNotes(totalExercises: Int, completedExercises: Int): String {
    val percentage = if (totalExercises > 0) {
        (completedExercises.toFloat() / totalExercises * 100).toInt()
    } else {
        0
    }

    return when {
        percentage == 100 -> "¡Workout completado al 100%! 💪🔥"
        percentage >= 80 -> "Gran esfuerzo, completado al $percentage% 💪"
        percentage >= 50 -> "Buen trabajo, completado al $percentage% 👍"
        else -> "Workout parcialmente completado ($percentage%)"
    }
}

@Preview
@Composable
fun WorkoutOfTheDayScreenPreview() {
    WorkoutOfTheDayScreen(
        onBackClick = {}
    )
}