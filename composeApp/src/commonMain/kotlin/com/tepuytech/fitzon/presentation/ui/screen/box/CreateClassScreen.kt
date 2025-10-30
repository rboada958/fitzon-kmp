package com.tepuytech.fitzon.presentation.ui.screen.box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.coaches
import com.tepuytech.fitzon.data.days
import com.tepuytech.fitzon.data.times
import com.tepuytech.fitzon.data.workoutsByDay
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class CreateClass : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        CreateClassScreen(
            onBackClick = {
                navigator.pop()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassScreen(
    onBackClick: () -> Unit = {},
) {

    var className by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedCoach by remember { mutableStateOf("") }
    var selectedWorkout by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }


    var expandedDay by remember { mutableStateOf(false) }
    var expandedTime by remember { mutableStateOf(false) }
    var expandedCoach by remember { mutableStateOf(false) }
    var expandedWorkout by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val isFormValid = className.isNotEmpty() && selectedDay.isNotEmpty() &&
            selectedTime.isNotEmpty() && selectedCoach.isNotEmpty() &&
            capacity.isNotEmpty()

    val platform = getPlatform()

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
                                    expandedDay = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hora
                Text(
                    text = "Hora de Inicio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedTime,
                    onExpandedChange = { expandedTime = it }
                ) {
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona hora", color = textGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedTime) "▲" else "▼",
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
                        expanded = expandedTime,
                        onDismissRequest = { expandedTime = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {
                        times.forEach { time ->
                            DropdownMenuItem(
                                text = { Text(time, color = Color.White, fontSize = 14.sp) },
                                onClick = {
                                    selectedTime = time
                                    expandedTime = false
                                }
                            )
                        }
                    }
                }

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
                        placeholder = { Text("Selecciona coachName", color = textGray) },
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
                        coaches.forEach { coach ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(coach.name, color = Color.White, fontSize = 14.sp)
                                        Text(coach.specialties.first(), color = textGray, fontSize = 12.sp)
                                    }
                                },
                                onClick = {
                                    selectedCoach = coach.name
                                    expandedCoach = false
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
                            val dayWorkouts = workoutsByDay[selectedDay] ?: emptyList()
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
                        onClick = { showSuccessDialog = true },
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

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showSuccessDialog) {
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
                            SuccessDetailRow("Clase:", className)
                            SuccessDetailRow("Día:", selectedDay)
                            SuccessDetailRow("Hora:", selectedTime)
                            SuccessDetailRow("Coach:", selectedCoach)
                            SuccessDetailRow("Workout:", selectedWorkout.ifEmpty { "Sin asignar" })
                            SuccessDetailRow("Capacidad:", capacity)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        className = ""
                        selectedDay = ""
                        selectedTime = ""
                        selectedCoach = ""
                        selectedWorkout = ""
                        capacity = ""
                        expandedDay = false
                        expandedTime = false
                        expandedCoach = false
                        expandedWorkout = false
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

@Preview
@Composable
fun PreviewCreateClassScreen() {
    CreateClassScreen()
}