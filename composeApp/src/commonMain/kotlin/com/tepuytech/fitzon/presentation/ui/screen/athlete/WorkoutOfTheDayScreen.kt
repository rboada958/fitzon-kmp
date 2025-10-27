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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.workout.ExerciseResponse
import com.tepuytech.fitzon.domain.model.workout.WorkoutResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class WorkoutOfTheDay(val workout: WorkoutResponse?) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WorkoutOfTheDayScreen(
            workout = workout,
            onBackClick = {
                navigator.pop()
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutOfTheDayScreen(
    workout: WorkoutResponse? = null,
    onBackClick: () -> Unit,
) {
    var exercises by remember { mutableStateOf(workout?.exercises ?: emptyList()) }

    var showCompletionDialog by remember { mutableStateOf(false) }
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
                                onEdit = { exerciseId ->
                                    // Handle edit action
                                },
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
                            if (allCompleted) {
                                showCompletionDialog = true
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
                    text = "Has completado el workout del día. ¡Sigue así!",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletionDialog = false
                        // Aquí podrías navegar a otra pantalla o guardar el progreso
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

@Preview
@Composable
fun WorkoutOfTheDayScreenPreview() {
    WorkoutOfTheDayScreen(
        onBackClick = {}
    )
}