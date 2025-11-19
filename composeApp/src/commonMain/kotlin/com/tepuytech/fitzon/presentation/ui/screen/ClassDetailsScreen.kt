package com.tepuytech.fitzon.presentation.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.classes.ClassDetailsResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.ClassUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackgroundAlpha
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.mapDayToSpanish
import com.tepuytech.fitzon.presentation.ui.composable.mapLevelToSpanish
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.viewmodel.ClassViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


class ClassDetails(val classId: String, val boxOwner: Boolean = true) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<ClassViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val enrollmentState by viewModel.enrollmentState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.classDetails(classId)
        }

        LaunchedEffect(enrollmentState) {
            if (enrollmentState is ClassUiState.EnrollmentSuccess ||
                enrollmentState is ClassUiState.UnenrollmentSuccess) {
                viewModel.classDetails(classId)
                viewModel.clearEnrollmentState()
            }
        }

        when (val state = uiState) {
            is ClassUiState.Loading -> {
                AthleteDashboardShimmer()
            }

            is ClassUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(brush = backgroundGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${state.message}", color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.classDetails(classId)}) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is ClassUiState.SuccessClassDetails -> {
                ClassDetailsScreen(
                    boxOwner = boxOwner,
                    classDetails = state.response,
                    onBackClick = {
                        navigator.pop()
                    },
                    onEnroll = {
                        viewModel.enrollInClass(classId)
                    },
                    onCancelEnrollment = {
                        viewModel.unenrollFromClass(classId)
                    },
                    enrollmentState = enrollmentState
                )
            }

            else -> {}
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailsScreen(
    classDetails: ClassDetailsResponse = ClassDetailsResponse(),
    onBackClick: () -> Unit = {},
    onEnroll: () -> Unit = {},
    onCancelEnrollment: () -> Unit = {},
    enrollmentState: ClassUiState,
    boxOwner: Boolean,
) {
    val platform = getPlatform()
    var showEnrolledList by remember { mutableStateOf(false) }
    var showExercises by remember { mutableStateOf(false) }

    val isProcessing = enrollmentState is ClassUiState.EnrollmentLoading &&
            enrollmentState.classId == classDetails.classId

    Scaffold(
        modifier = Modifier.background(brush = backgroundGradient),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles de Clase",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = backgroundGradient),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header con nombre y badge de inscripción
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = cardBackgroundAlpha
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = classDetails.className,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )

                            if (classDetails.isEnrolled) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = greenPrimary.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "✓ INSCRITO",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = greenPrimary,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }
                        }

                        // Level badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = greenPrimary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = mapLevelToSpanish(classDetails.level),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = greenLight,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = cardBackgroundAlpha
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailRow("📅", "Día", mapDayToSpanish(classDetails.dayOfWeek))
                        DetailRow("🕐", "Hora", "${classDetails.startTime} - ${classDetails.endTime}")
                        
                        // Coach con información adicional
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("👨‍🏫", fontSize = 20.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Coach",
                                    fontSize = 12.sp,
                                    color = textGray,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = classDetails.coach.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                if (classDetails.coach.specialty.isNotEmpty()) {
                                    Text(
                                        text = classDetails.coach.specialty,
                                        fontSize = 12.sp,
                                        color = greenLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Capacidad y progreso de inscripción
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = cardBackgroundAlpha
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Capacidad",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenLight
                            )

                            Text(
                                text = "${classDetails.currentEnrollment}/${classDetails.maxCapacity}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        LinearProgressIndicator(
                            progress = {
                                classDetails.currentEnrollment.toFloat() / classDetails.maxCapacity
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            color = when {
                                classDetails.spotsLeft <= 0 -> Color(0xFFFF6B6B)
                                classDetails.spotsLeft <= 3 -> Color(0xFFFFB84D)
                                else -> greenPrimary
                            },
                            trackColor = Color(0xFF2D6A4F),
                        )

                        Text(
                            text = when {
                                classDetails.spotsLeft <= 0 -> "⚠️ Clase llena"
                                classDetails.spotsLeft == 1 -> "⚠️ Último cupo disponible"
                                else -> "✓ ${classDetails.spotsLeft} cupos disponibles"
                            },
                            fontSize = 13.sp,
                            color = when {
                                classDetails.spotsLeft <= 0 -> Color(0xFFFF6B6B)
                                classDetails.spotsLeft <= 3 -> Color(0xFFFFB84D)
                                else -> greenPrimary
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Workout asignado con ejercicios
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = greenPrimary.copy(alpha = 0.15f)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Workout Asignado",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = greenLight
                                )
                                Text(
                                    text = "💪 ${classDetails.workout.title}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Button(
                                onClick = { showExercises = !showExercises },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Text(
                                    if (showExercises) "▼ Ver menos" else "▶ Ver ejercicios",
                                    fontSize = 12.sp,
                                    color = Color(0xFF081C15),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Info del workout
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "⏱️ Duración",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB7B7B7)
                                )
                                Text(
                                    text = "${classDetails.workout.duration} min",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "📊 Dificultad",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB7B7B7)
                                )
                                Text(
                                    text = classDetails.workout.difficulty,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "💪 Ejercicios",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB7B7B7)
                                )
                                Text(
                                    text = classDetails.workout.exercises.size.toString(),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        // Descripción del workout
                        if (classDetails.workout.description.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                thickness = 1.dp,
                                color = Color(0xFF2D6A4F)
                            )

                            Text(
                                text = classDetails.workout.description,
                                fontSize = 13.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }

                        // Lista de ejercicios
                        if (showExercises && classDetails.workout.exercises.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                thickness = 1.dp,
                                color = Color(0xFF2D6A4F)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                classDetails.workout.exercises.forEach { exercise ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF0D2818)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = exercise.name,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = greenLight
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Text(
                                                    text = "Sets: ${exercise.sets}",
                                                    fontSize = 12.sp,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = "Reps: ${exercise.reps}",
                                                    fontSize = 12.sp,
                                                    color = Color.White
                                                )
                                                if (!exercise.weight.isNullOrEmpty()) {
                                                    Text(
                                                        text = "Peso: ${exercise.weight}",
                                                        fontSize = 12.sp,
                                                        color = greenPrimary,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Atletas inscritos
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = cardBackgroundAlpha
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Atletas Inscritos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenLight
                            )

                            if (classDetails.enrolledAthletes.isNotEmpty()) {
                                Button(
                                    onClick = { showEnrolledList = !showEnrolledList },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = greenPrimary.copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(36.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    )
                                ) {
                                    Text(
                                        "${classDetails.enrolledAthletes.size}",
                                        fontSize = 12.sp,
                                        color = greenPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Lista de inscritos
                        if (showEnrolledList && classDetails.enrolledAthletes.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = Color(0xFF2D6A4F)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                classDetails.enrolledAthletes.forEach { athlete ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Avatar
                                        Surface(
                                            modifier = Modifier.size(36.dp),
                                            shape = RoundedCornerShape(50),
                                            color = Color(0xFF2D6A4F)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Text(
                                                    text = athlete.name.first().toString(),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = greenLight
                                                )
                                            }
                                        }

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = athlete.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (classDetails.enrolledAthletes.isEmpty()) {
                            Text(
                                text = "Sin inscritos aún",
                                fontSize = 13.sp,
                                color = textGray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }

            item {
                if (!boxOwner) { // Ver botón de inscripción si es atleta
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!classDetails.isEnrolled && classDetails.spotsLeft > 0) {
                            Button(
                                onClick = onEnroll,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isProcessing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        "✓ Inscribirse a esta clase",
                                        color = Color(0xFF081C15),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else if (classDetails.isEnrolled) {
                            OutlinedButton(
                                onClick = onCancelEnrollment,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFFF6B6B)
                                ),
                                border = BorderStroke(2.dp, Color(0xFFFF6B6B)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isProcessing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        "✕ Cancelar inscripción",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                enabled = false,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2D6A4F)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "⚠️ Clase llena",
                                    color = textGray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: String,
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = textGray,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassDetailsScreenPreview() {

    ClassDetailsScreen(
        boxOwner = true,
        enrollmentState = ClassUiState.Idle,
    )
}