package com.tepuytech.fitzon.presentation.ui.screen.box

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.tepuytech.fitzon.domain.model.ExerciseInput
import com.tepuytech.fitzon.domain.model.workout.CreateExerciseRequest
import com.tepuytech.fitzon.domain.model.workout.CreateWorkoutRequest
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.WorkoutUiState
import com.tepuytech.fitzon.presentation.ui.composable.*
import com.tepuytech.fitzon.presentation.viewmodel.WorkoutViewModel
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

class CreateWorkout() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WorkoutViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        var showSuccessDialog by remember { mutableStateOf(false) }
        var createdWorkoutTitle by remember { mutableStateOf("") }

        LaunchedEffect(uiState) {
            when (uiState) {
                is WorkoutUiState.SuccessCreateWorkout -> {
                    showSuccessDialog = true
                }
                else -> {}
            }
        }

        CreateWorkoutScreen(
            onBackClick = { navigator.pop() },
            onCreateWorkout = { workout ->
                createdWorkoutTitle = workout.title
                viewModel.createWorkout(workout)
            }
        )

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

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Text(
                        text = "✓ Workout Creado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                text = {
                    Column {
                        Text("El workout \"$createdWorkoutTitle\" ha sido creado exitosamente", fontSize = 15.sp)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navigator.pop()
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
fun CreateWorkoutScreen(
    onBackClick: () -> Unit = {},
    onCreateWorkout: (CreateWorkoutRequest) -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("RX") }
    var exercises by remember { mutableStateOf(listOf<ExerciseInput>()) }
    var errorMessage by remember { mutableStateOf("") }

    var expandedDifficulty by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val platform = getPlatform()
    val difficulties = listOf("RX", "Scaled", "RX / Scaled", "Beginner", "Intermediate", "Advanced")

    val isFormValid = title.isNotEmpty() &&
            date.isNotEmpty() &&
            duration.isNotEmpty() &&
            exercises.isNotEmpty() &&
            exercises.all { it.name.isNotEmpty() && it.sets.isNotEmpty() && it.reps.isNotEmpty() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Workout",
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
                // Título
                Text(
                    text = "Título del Workout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ej: Cindy, Fran, Helen", color = textGray) },
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
                    placeholder = { Text("Describe el workout", color = textGray) },
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

                // Fecha y Duración en Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fecha
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Fecha",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = date,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("YYYY-MM-DD", color = textGray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDatePicker = true },
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                Text(text = "📅", fontSize = 20.sp)
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
                    }

                    // Duración
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Duración (min)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            placeholder = { Text("45", color = textGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Dificultad
                Text(
                    text = "Dificultad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDifficulty,
                    onExpandedChange = { expandedDifficulty = it }
                ) {
                    OutlinedTextField(
                        value = selectedDifficulty,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedDifficulty) "▲" else "▼",
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
                        expanded = expandedDifficulty,
                        onDismissRequest = { expandedDifficulty = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {
                        difficulties.forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty, color = Color.White, fontSize = 14.sp) },
                                onClick = {
                                    selectedDifficulty = difficulty
                                    expandedDifficulty = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Ejercicios Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ejercicios",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Button(
                        onClick = {
                            exercises = exercises + ExerciseInput(
                                id = exercises.size,
                                name = "",
                                sets = "",
                                reps = "",
                                rxVersion = "",
                                scaledVersion = ""
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar ejercicio",
                            tint = Color(0xFF081C15)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar", color = Color(0xFF081C15))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de ejercicios
                exercises.forEachIndexed { index, exercise ->
                    ExerciseInputCard(
                        exercise = exercise,
                        onExerciseChange = { updated ->
                            exercises = exercises.toMutableList().apply {
                                set(index, updated)
                            }
                        },
                        onDelete = {
                            exercises = exercises.filterIndexed { i, _ -> i != index }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (exercises.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFB84D).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ℹ️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Agrega al menos un ejercicio al workout",
                                fontSize = 14.sp,
                                color = Color(0xFFFFB84D)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
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

                            val durationInt = duration.toIntOrNull()
                            if (durationInt == null || durationInt <= 0) {
                                errorMessage = "La duración debe ser un número mayor a 0"
                                return@Button
                            }

                            val dayOfWeek = try {
                                val parts = date.split("-")
                                val year = parts[0].toInt()
                                val month = parts[1].toInt()
                                val day = parts[2].toInt()

                                val localDate = LocalDate(year, month, day)
                                localDate.dayOfWeek.name  // "MONDAY", "TUESDAY", etc.
                            } catch (e: Exception) {
                                println("Error calculating dayOfWeek: ${e.message}")
                                "MONDAY"  // Default fallback
                            }

                            val createWorkoutRequest = CreateWorkoutRequest(
                                title = title,
                                description = description.ifEmpty { "Workout de $title" },
                                date = date,
                                dayOfWeek = dayOfWeek,
                                duration = durationInt,
                                difficulty = selectedDifficulty,
                                exercises = exercises.map { ex ->
                                    CreateExerciseRequest(
                                        name = ex.name,
                                        sets = ex.sets.toIntOrNull() ?: 0,
                                        reps = ex.reps.toIntOrNull() ?: 0,
                                        rx_version = ex.rxVersion.ifEmpty { null },
                                        scaled_version = ex.scaledVersion.ifEmpty { null }
                                    )
                                }
                            )

                            onCreateWorkout(createWorkoutRequest)
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
                            "✓ Crear Workout",
                            color = if (isFormValid) Color(0xFF081C15) else textGray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                date = selectedDate
                showDatePicker = false
            }
        )
    }
}

@Composable
fun ExerciseInputCard(
    exercise: ExerciseInput,
    onExerciseChange: (ExerciseInput) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0D2818),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ejercicio ${exercise.id + 1}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = greenLight
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFFF5252)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del ejercicio
            OutlinedTextField(
                value = exercise.name,
                onValueChange = { onExerciseChange(exercise.copy(name = it)) },
                label = { Text("Nombre", color = textGray, fontSize = 14.sp) },
                placeholder = { Text("Run, Pull-ups, etc.", color = textGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = greenLight,
                    unfocusedBorderColor = Color(0xFF2D6A4F),
                    focusedContainerColor = Color(0xFF1B4332),
                    unfocusedContainerColor = Color(0xFF1B4332)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sets y Reps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = exercise.sets,
                    onValueChange = { onExerciseChange(exercise.copy(sets = it)) },
                    label = { Text("Sets", color = textGray, fontSize = 14.sp) },
                    placeholder = { Text("10", color = textGray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF1B4332),
                        unfocusedContainerColor = Color(0xFF1B4332)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = exercise.reps,
                    onValueChange = { onExerciseChange(exercise.copy(reps = it)) },
                    label = { Text("Reps", color = textGray, fontSize = 14.sp) },
                    placeholder = { Text("20", color = textGray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF1B4332),
                        unfocusedContainerColor = Color(0xFF1B4332)
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // RX Version
            OutlinedTextField(
                value = exercise.rxVersion,
                onValueChange = { onExerciseChange(exercise.copy(rxVersion = it)) },
                label = { Text("RX Version", color = textGray, fontSize = 14.sp) },
                placeholder = { Text("Strict/Kipping", color = textGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = greenLight,
                    unfocusedBorderColor = Color(0xFF2D6A4F),
                    focusedContainerColor = Color(0xFF1B4332),
                    unfocusedContainerColor = Color(0xFF1B4332)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Scaled Version
            OutlinedTextField(
                value = exercise.scaledVersion,
                onValueChange = { onExerciseChange(exercise.copy(scaledVersion = it)) },
                label = { Text("Scaled Version", color = textGray, fontSize = 14.sp) },
                placeholder = { Text("Ring Rows", color = textGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = greenLight,
                    unfocusedBorderColor = Color(0xFF2D6A4F),
                    focusedContainerColor = Color(0xFF1B4332),
                    unfocusedContainerColor = Color(0xFF1B4332)
                ),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = LocalDate.fromEpochDays((millis / 86400000).toInt())
                        onDateSelected(localDate.toString())
                    }
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
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF1B4332),
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.White,
                subheadContentColor = Color.White,
                yearContentColor = Color.White,
                currentYearContentColor = greenPrimary,
                selectedYearContentColor = Color(0xFF081C15),
                selectedYearContainerColor = greenPrimary,
                dayContentColor = Color.White,
                selectedDayContentColor = Color(0xFF081C15),
                selectedDayContainerColor = greenPrimary,
                todayContentColor = greenLight,
                todayDateBorderColor = greenLight
            )
        )
    }
}

@Preview
@Composable
fun PreviewCreateWorkoutScreen() {
    CreateWorkoutScreen()
}