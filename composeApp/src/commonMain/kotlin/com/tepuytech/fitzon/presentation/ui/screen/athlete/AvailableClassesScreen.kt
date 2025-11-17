package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponse
import com.tepuytech.fitzon.domain.model.athletes.AvailableClassesResponseItem
import com.tepuytech.fitzon.presentation.state.ClassUiState
import com.tepuytech.fitzon.presentation.ui.composable.*
import com.tepuytech.fitzon.presentation.viewmodel.ClassViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class AvailableClasses : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<ClassViewModel>()
        val uiState by viewModel.availableClassesState.collectAsState()
        val enrollmentState by viewModel.enrollmentState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadAvailableClasses()
        }

        LaunchedEffect(enrollmentState) {
            if (enrollmentState is ClassUiState.EnrollmentSuccess ||
                enrollmentState is ClassUiState.UnenrollmentSuccess) {
                viewModel.loadAvailableClasses()
            }
        }

        AvailableClassesScreen(
            uiState = uiState,
            enrollmentState = enrollmentState,
            onBackClick = { navigator.pop() },
            onEnrollClick = { classId -> viewModel.enrollInClass(classId) },
            onUnenrollClick = { classId -> viewModel.unenrollFromClass(classId) },
            onRetry = { viewModel.loadAvailableClasses() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableClassesScreen(
    uiState: ClassUiState,
    enrollmentState: ClassUiState,
    onBackClick: () -> Unit = {},
    onEnrollClick: (String) -> Unit = {},
    onUnenrollClick: (String) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val filteredClasses = remember(uiState, selectedDay) {
        if (uiState is ClassUiState.AvailableClassesLoaded) {
            if (selectedDay == null) {
                uiState.classes.classes
            } else {
                uiState.classes.classes.filter { it.dayOfWeek == selectedDay }
            }
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Clases Disponibles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
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
            when (uiState) {
                is ClassUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = greenPrimary)
                    }
                }

                is ClassUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text("⚠️", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Error al cargar clases",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                uiState.message,
                                fontSize = 14.sp,
                                color = textGray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onRetry,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                is ClassUiState.AvailableClassesLoaded -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Filtros por día
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn() + slideInVertically()
                        ) {
                            DayFilterRow(
                                selectedDay = selectedDay,
                                onDaySelected = { day ->
                                    selectedDay = day
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (filteredClasses.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Text("📅", fontSize = 64.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No hay clases disponibles",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        if (selectedDay != null)
                                            "Intenta con otro día"
                                        else
                                            "Aún no hay clases programadas",
                                        fontSize = 14.sp,
                                        color = textGray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn() + slideInVertically(
                                    animationSpec = tween(500, delayMillis = 100)
                                )
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val groupedClasses = filteredClasses.groupBy { it.dayOfWeek }

                                    groupedClasses.forEach { (dayOfWeek, classes) ->
                                        item {
                                            Text(
                                                text = mapDayToSpanish(dayOfWeek),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = greenLight,
                                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                            )
                                        }

                                        items(classes) { classItem ->
                                            AvailableClassCard(
                                                classItem = classItem,
                                                enrollmentState = enrollmentState,
                                                onEnrollClick = { onEnrollClick(classItem.id) },
                                                onUnenrollClick = { onUnenrollClick(classItem.id) }
                                            )
                                        }
                                    }

                                    item {
                                        Spacer(modifier = Modifier.height(80.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun DayFilterRow(
    selectedDay: String?,
    onDaySelected: (String?) -> Unit
) {
    val days = listOf(
        null to "Todos",
        "MONDAY" to "Lun",
        "TUESDAY" to "Mar",
        "WEDNESDAY" to "Mié",
        "THURSDAY" to "Jue",
        "FRIDAY" to "Vie",
        "SATURDAY" to "Sáb",
        "SUNDAY" to "Dom"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { (day, label) ->
            FilterChip(
                selected = selectedDay == day,
                onClick = { onDaySelected(day) },
                label = {
                    Text(
                        label,
                        fontSize = 14.sp,
                        fontWeight = if (selectedDay == day) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = greenPrimary,
                    selectedLabelColor = Color(0xFF081C15),
                    containerColor = cardBackground,
                    labelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun AvailableClassCard(
    classItem: AvailableClassesResponseItem,
    enrollmentState: ClassUiState,
    onEnrollClick: () -> Unit,
    onUnenrollClick: () -> Unit
) {
    val isProcessing = enrollmentState is ClassUiState.EnrollmentLoading &&
            enrollmentState.classId == classItem.id

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardBackground,
        shadowElevation = if (classItem.isEnrolled) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = classItem.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        if (classItem.isEnrolled) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = greenPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "✓ INSCRITO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = greenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        if (classItem.workoutTitle != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFF6B6B).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "WOD",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B6B),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⏰", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${classItem.startTime} - ${classItem.endTime}",
                            fontSize = 14.sp,
                            color = textGray
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👨‍🏫", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = classItem.coachName,
                            fontSize = 14.sp,
                            color = textGray
                        )
                    }

                    if (classItem.workoutTitle != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏋️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = classItem.workoutTitle ?: "",
                                fontSize = 14.sp,
                                color = greenLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Level Badge
                Surface(
                    shape = CircleShape,
                    color = when (classItem.level) {
                        "BEGINNER" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        "INTERMEDIATE" -> Color(0xFFFF9800).copy(alpha = 0.2f)
                        "ADVANCED" -> Color(0xFFFF5252).copy(alpha = 0.2f)
                        else -> cardBackground
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (classItem.level) {
                                "BEGINNER" -> "B"
                                "INTERMEDIATE" -> "I"
                                "ADVANCED" -> "A"
                                else -> "?"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (classItem.level) {
                                "BEGINNER" -> Color(0xFF4CAF50)
                                "INTERMEDIATE" -> Color(0xFFFF9800)
                                "ADVANCED" -> Color(0xFFFF5252)
                                else -> Color.White
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Capacity Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cupos disponibles",
                    fontSize = 12.sp,
                    color = textGray
                )
                Text(
                    text = "${classItem.spotsLeft}/${classItem.maxCapacity}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (classItem.spotsLeft > 0) greenLight else Color(0xFFFF6B6B)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { classItem.currentEnrollment.toFloat() / classItem.maxCapacity },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = if (classItem.spotsLeft > 3) greenPrimary else Color(0xFFFF9800),
                trackColor = Color(0xFF1B4332),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            if (classItem.spotsLeft > 0 || classItem.isEnrolled) {
                Button(
                    onClick = {
                        if (classItem.isEnrolled) {
                            onUnenrollClick()
                        } else {
                            onEnrollClick()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (classItem.isEnrolled)
                            Color(0xFFFF6B6B)
                        else greenPrimary,
                        disabledContainerColor = cardBackground
                    )
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (classItem.isEnrolled) "Cancelar Inscripción" else "Inscribirse",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (classItem.isEnrolled) Color.White else Color(0xFF081C15),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1B4332)
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔒 Clase Llena",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textGray
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AvailableClassesScreenPreview() {
    AvailableClassesScreen(
        uiState = ClassUiState.AvailableClassesLoaded(
            AvailableClassesResponse(
                classes = listOf()
            )
        ),
        enrollmentState = ClassUiState.Idle
    )
}