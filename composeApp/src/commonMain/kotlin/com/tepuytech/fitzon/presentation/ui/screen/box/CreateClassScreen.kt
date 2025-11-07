package com.tepuytech.fitzon.presentation.ui.screen.box

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.model.WorkoutOption
import com.tepuytech.fitzon.domain.model.classes.CreateClassRequest
import com.tepuytech.fitzon.domain.model.coach.CoachResponse
import com.tepuytech.fitzon.domain.model.workout.BoxWorkoutResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.ClassUiState
import com.tepuytech.fitzon.presentation.state.CoachUiState
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.viewmodel.ClassViewModel
import com.tepuytech.fitzon.presentation.viewmodel.CoachViewModel
import com.tepuytech.fitzon.presentation.viewmodel.WorkoutViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class CreateClass(val boxId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val classViewModel = getScreenModel<ClassViewModel>()
        val classUiState by classViewModel.uiState.collectAsState()

        val coachViewModel = getScreenModel<CoachViewModel>()
        val coachUiState by coachViewModel.uiState.collectAsState()

        val workoutViewModel = getScreenModel<WorkoutViewModel>()
        val workoutUiState by workoutViewModel.uiState.collectAsState()

        var showSuccessDialog by remember { mutableStateOf(false) }
        var createdClassData by remember { mutableStateOf<CreateClassRequest?>(null) }
        var coachName by remember { mutableStateOf("") }
        var workoutName by remember { mutableStateOf("") }
        var shouldClearFields by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            coachViewModel.getCoaches()
        }

        LaunchedEffect(coachUiState) {
            if (coachUiState is CoachUiState.Success || coachUiState is CoachUiState.Empty) {
                workoutViewModel.boxWorkout(boxId)
            }
        }

        val isLoading =
            coachUiState is CoachUiState.Loading ||
                    workoutUiState is WorkoutUiState.Loading ||
                    (coachUiState is CoachUiState.Success && workoutUiState !is WorkoutUiState.SuccessBoxWorkout)

        if (isLoading) {
            AthleteDashboardShimmer()
            return
        }

        if (coachUiState is CoachUiState.Success && workoutUiState is WorkoutUiState.SuccessBoxWorkout) {
            val coachState = coachUiState as CoachUiState.Success
            val workoutState = workoutUiState as WorkoutUiState.SuccessBoxWorkout

            val workoutsByDay = remember(workoutState.boxWorkoutData) {
                groupWorkoutsByDay(workoutState.boxWorkoutData)
            }

            CreateClassScreen(
                coachesState = coachState.coaches,
                workoutState = workoutsByDay,
                onBackClick = {
                    navigator.pop()
                },
                onCreateClassClick = {
                    classViewModel.createClass(it)
                    createdClassData = it
                    println(it.toString())
                },
                onNames = { coach, workout ->
                    coachName = coach
                    workoutName = workout
                },
                clearFields = shouldClearFields,
                onFieldsCleared = { shouldClearFields = false }
            )
        }

        LaunchedEffect(classUiState) {
            when (classUiState) {
                is ClassUiState.SuccessCreateClass -> {
                    showSuccessDialog = true
                }
                else -> {}
            }
        }

        if (classUiState is ClassUiState.Loading) {
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

        if (showSuccessDialog) {
            val data = createdClassData!!
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Text(
                        text = "✓ Clase Creada",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                text = {
                    Column {
                        Text("La clase ha sido creada exitosamente", fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0D2818)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                SuccessDetailRow("Clase:", data.name)
                                SuccessDetailRow("Descripción:", data.description)
                                SuccessDetailRow("Día:", data.dayOfWeek)
                                SuccessDetailRow("Inicio:", data.startTime)
                                SuccessDetailRow("Fin:", data.endTime)
                                SuccessDetailRow("Coach:", coachName)
                                SuccessDetailRow("Nivel:", data.level)
                                SuccessDetailRow("Workout:", workoutName)
                                SuccessDetailRow("Capacidad:", data.maxCapacity.toString())
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            shouldClearFields = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        )
                    ) {
                        Text("Volver", color = Color(0xFF081C15))
                    }
                },
                containerColor = Color(0xFF1B4332),
                titleContentColor = Color.White,
                textContentColor = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassScreen(
    coachesState: List<CoachResponse> = emptyList(),
    workoutState: Map<String, List<WorkoutOption>> = emptyMap(),
    onBackClick: () -> Unit = {},
    onCreateClassClick: (CreateClassRequest) -> Unit = {},
    onNames: (String, String) -> Unit = { _, _ -> },
    clearFields: Boolean = false,
    onFieldsCleared: () -> Unit = {}
) {
    var className by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var selectedCoach by remember { mutableStateOf("") }
    var selectedCoachId by remember { mutableStateOf("") }
    var selectedWorkout by remember { mutableStateOf("") }
    var selectedWorkoutId by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("BEGINNER") }

    var errorMessage by remember { mutableStateOf("") }

    var expandedDay by remember { mutableStateOf(false) }
    var expandedCoach by remember { mutableStateOf(false) }
    var expandedWorkout by remember { mutableStateOf(false) }
    var expandedLevel by remember { mutableStateOf(false) }

    // Estados para los time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val isFormValid = className.isNotEmpty() && selectedDay.isNotEmpty() &&
            startTime.isNotEmpty() && endTime.isNotEmpty() && selectedCoach.isNotEmpty() &&
            capacity.isNotEmpty()

    val platform = getPlatform()

    val levels = listOf("BEGINNER", "INTERMEDIATE", "ADVANCED")
    val levelsSpanish = mapOf(
        "BEGINNER" to "Principiante",
        "INTERMEDIATE" to "Intermedio",
        "ADVANCED" to "Avanzado"
    )

    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    LaunchedEffect(clearFields) {
        if (clearFields) {
            className = ""
            description = ""
            selectedDay = ""
            startTime = ""
            endTime = ""
            selectedCoach = ""
            selectedCoachId = ""
            selectedWorkout = ""
            selectedWorkoutId = ""
            capacity = ""
            selectedLevel = "BEGINNER"
            errorMessage = ""
            onFieldsCleared()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Nueva Clase",
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
                    .padding(20.dp)
            ) {
                // Nombre de la clase
                Text(
                    text = "Nombre de la Clase",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    placeholder = { Text("Ej: Morning WOD", color = textGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Descripción
                Text(
                    text = "Descripción",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Descripción de la clase", color = textGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818)
                    ),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Día
                Text(
                    text = "Día de la Semana",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDay,
                    onExpandedChange = { expandedDay = it }
                ) {
                    OutlinedTextField(
                        value = selectedDay,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona día", color = textGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedDay) "▲" else "▼",
                                color = textGray,
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = greenLight,
                            unfocusedBorderColor = Color(0xFF2D6A4F),
                            focusedContainerColor = Color(0xFF0D2818),
                            unfocusedContainerColor = Color(0xFF0D2818)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDay,
                        onDismissRequest = { expandedDay = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {
                        days.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day, color = Color.White, fontSize = 14.sp) },
                                onClick = {
                                    selectedDay = day
                                    selectedWorkout = ""
                                    selectedWorkoutId = ""
                                    expandedDay = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // HORA DE INICIO
                Text(
                    text = "Hora de Inicio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = startTime,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona hora de inicio", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartTimePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Text(
                            text = "🕐",
                            fontSize = 20.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        disabledBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818),
                        disabledContainerColor = Color(0xFF0D2818)
                    ),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // HORA DE FIN
                Text(
                    text = "Hora de Fin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = endTime,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona hora de fin", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndTimePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Text(
                            text = "🕐",
                            fontSize = 20.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        disabledBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818),
                        disabledContainerColor = Color(0xFF0D2818)
                    ),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Coach
                Text(
                    text = "Coach",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCoach,
                    onExpandedChange = { expandedCoach = it }
                ) {
                    OutlinedTextField(
                        value = selectedCoach,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona coach", color = textGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedCoach) "▲" else "▼",
                                color = textGray,
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = greenLight,
                            unfocusedBorderColor = Color(0xFF2D6A4F),
                            focusedContainerColor = Color(0xFF0D2818),
                            unfocusedContainerColor = Color(0xFF0D2818)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCoach,
                        onDismissRequest = { expandedCoach = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {
                        coachesState.forEach { coach ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(coach.name, color = Color.White, fontSize = 14.sp)
                                        Text(coach.specialties.first(), color = textGray, fontSize = 12.sp)
                                    }
                                },
                                onClick = {
                                    selectedCoach = coach.name
                                    selectedCoachId = coach.id
                                    expandedCoach = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nivel
                Text(
                    text = "Nivel de la Clase",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedLevel,
                    onExpandedChange = { expandedLevel = it }
                ) {
                    OutlinedTextField(
                        value = levelsSpanish[selectedLevel] ?: "Principiante",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedLevel) "▲" else "▼",
                                color = textGray,
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = greenLight,
                            unfocusedBorderColor = Color(0xFF2D6A4F),
                            focusedContainerColor = Color(0xFF0D2818),
                            unfocusedContainerColor = Color(0xFF0D2818)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedLevel,
                        onDismissRequest = { expandedLevel = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {
                        levels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(levelsSpanish[level] ?: level, color = Color.White, fontSize = 14.sp) },
                                onClick = {
                                    selectedLevel = level
                                    expandedLevel = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Capacidad
                Text(
                    text = "Capacidad Máxima",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    placeholder = { Text("Ej: 15", color = textGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (selectedDay.isNotEmpty()) {
                    Text(
                        text = "Seleccionar Workout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Workouts disponibles para $selectedDay",
                        fontSize = 13.sp,
                        color = greenLight,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedWorkout,
                        onExpandedChange = { expandedWorkout = it }
                    ) {
                        OutlinedTextField(
                            value = selectedWorkout,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Selecciona workout", color = textGray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                Text(
                                    text = if (expandedWorkout) "▲" else "▼",
                                    color = textGray,
                                    fontSize = 12.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = greenLight,
                                unfocusedBorderColor = Color(0xFF2D6A4F),
                                focusedContainerColor = Color(0xFF0D2818),
                                unfocusedContainerColor = Color(0xFF0D2818)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedWorkout,
                            onDismissRequest = { expandedWorkout = false },
                            modifier = Modifier.background(Color(0xFF1B4332))
                        ) {
                            val dayWorkouts = workoutState[mapDayToEnglish(selectedDay)] ?: emptyList()
                            dayWorkouts.forEach { workout ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(workout.name, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = greenPrimary.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        workout.type,
                                                        color = greenLight,
                                                        fontSize = 11.sp,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Text(workout.description, color = textGray, fontSize = 12.sp)
                                            Text("${workout.exerciseCount} ejercicios", color = textGray, fontSize = 11.sp)
                                        }
                                    },
                                    onClick = {
                                        selectedWorkout = workout.name
                                        selectedWorkoutId = workout.id
                                        expandedWorkout = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (selectedWorkout.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = greenPrimary.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✓", fontSize = 20.sp, color = greenPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Workout seleccionado:",
                                        fontSize = 12.sp,
                                        color = textGray
                                    )
                                    Text(
                                        selectedWorkout,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFB84D).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ℹ️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Selecciona un día",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFB84D)
                                )
                                Text(
                                    "para ver workouts disponibles",
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onBackClick() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = greenLight
                        )
                    ) {
                        Text("Cancelar", modifier = Modifier.padding(vertical = 4.dp))
                    }

                    Button(
                        onClick = {
                            errorMessage = ""

                            val dayOfWeek = mapDayToEnglish(selectedDay)
                            val maxCapacity = capacity.toIntOrNull() ?: 0

                            if (maxCapacity <= 0) {
                                errorMessage = "La capacidad debe ser un número mayor a 0"
                                return@Button
                            }

                            // Crear request object
                            val createClassRequest = CreateClassRequest(
                                className,
                                selectedCoachId,
                                description.ifEmpty { "Clase de $className" },
                                startTime,
                                endTime,
                                dayOfWeek,
                                maxCapacity,
                                selectedLevel,
                                selectedWorkoutId
                            )

                            onCreateClassClick(createClassRequest)
                            onNames(selectedCoach, selectedWorkout)
                        },
                        enabled = isFormValid,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary,
                            disabledContainerColor = Color(0xFF2D6A4F)
                        )
                    ) {
                        Text(
                            "✓ Crear Clase",
                            color = if (isFormValid) Color(0xFF081C15) else textGray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ver error si existe
                if (errorMessage.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFF5252).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚠️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Error",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFF5252),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    errorMessage,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Time Picker Dialogs
    if (showStartTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = formatTime(hour, minute)
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = formatTime(hour, minute)
                showEndTimePicker = false
            }
        )
    }
}

@Composable
fun SuccessDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFFB7B7B7),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF74C69D)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 6,
        initialMinute = 0,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Seleccionar Hora",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color(0xFF0D2818),
                    clockDialSelectedContentColor = Color.White,
                    clockDialUnselectedContentColor = textGray,
                    selectorColor = greenPrimary,
                    containerColor = Color(0xFF1B4332),
                    periodSelectorBorderColor = greenLight,
                    periodSelectorSelectedContainerColor = greenPrimary,
                    periodSelectorUnselectedContainerColor = Color(0xFF0D2818),
                    periodSelectorSelectedContentColor = Color(0xFF081C15),
                    periodSelectorUnselectedContentColor = Color.White,
                    timeSelectorSelectedContainerColor = greenPrimary,
                    timeSelectorUnselectedContainerColor = Color(0xFF0D2818),
                    timeSelectorSelectedContentColor = Color(0xFF081C15),
                    timeSelectorUnselectedContentColor = Color.White
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = greenPrimary
                )
            ) {
                Text("Confirmar", color = Color(0xFF081C15))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar", color = greenLight)
            }
        },
        containerColor = Color(0xFF1B4332),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}


fun mapDayToEnglish(day: String): String {
    return when (day) {
        "Lunes" -> "MONDAY"
        "Martes" -> "TUESDAY"
        "Miércoles" -> "WEDNESDAY"
        "Jueves" -> "THURSDAY"
        "Viernes" -> "FRIDAY"
        "Sábado" -> "SATURDAY"
        "Domingo" -> "SUNDAY"
        else -> day
    }
}

private fun formatTime(hour: Int, minute: Int): String {
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when (hour) {
        0 -> 12
        in 1..12 -> hour
        else -> hour - 12
    }
    return "$displayHour:${minute.toString().padStart(2, '0')} $period"
}

private fun groupWorkoutsByDay(boxWorkouts: List<BoxWorkoutResponse>): Map<String, List<WorkoutOption>> {
    return boxWorkouts
        .groupBy { it.dayOfWeek }
        .mapValues { (_, workouts) ->
            workouts.map { workout ->
                WorkoutOption(
                    id = workout.id,
                    name = workout.title,
                    type = workout.difficulty,
                    description = workout.description,
                    exerciseCount = workout.exercises.size
                )
            }
        }
}

@Preview
@Composable
fun PreviewCreateClassScreen() {
    CreateClassScreen()
}