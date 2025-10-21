package com.tepuytech.fitzon.presentation.ui.screen.box

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.classes
import com.tepuytech.fitzon.domain.enums.ClassStatus
import com.tepuytech.fitzon.domain.enums.DayOfWeek
import com.tepuytech.fitzon.domain.model.ClassSession
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.FitzonQuickStat
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class ManageClasses : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageClassesScreen(
            onBackClick = { navigator.pop() },
            onCreateClassClick = { navigator.push(CreateClass()) }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClassesScreen(
    onBackClick: () -> Unit = {},
    onCreateClassClick: () -> Unit = {},
) {
    var classes by remember {
        mutableStateOf(
            classes
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf<DayOfWeek?>(null) }
    var selectedClass by remember { mutableStateOf<ClassSession?>(null) }
    var showClassDetails by remember { mutableStateOf(false) }

    val days = listOf(DayOfWeek.LUNES, DayOfWeek.MARTES, DayOfWeek.MIERCOLES, DayOfWeek.JUEVES, DayOfWeek.VIERNES, DayOfWeek.SABADO, DayOfWeek.DOMINGO)


    val filteredClasses = classes.filter { classSession ->
        val matchesSearch = classSession.name.contains(searchQuery, ignoreCase = true) ||
                classSession.coach.contains(searchQuery, ignoreCase = true)
        val matchesDay = selectedDay == null ||
                classSession.day.replace("á", "a")
                    .replace("é", "e")
                    .replace("í", "i")
                    .replace("ó", "o")
                    .replace("ú", "u")
                    .equals(selectedDay?.name, ignoreCase = true)
        matchesSearch && matchesDay
    }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Horarios y Clases",
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
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onCreateClassClick() },
                containerColor = greenPrimary,
                contentColor = Color(0xFF081C15)
            ) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Clase", fontWeight = FontWeight.Bold)
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
                modifier = Modifier.fillMaxSize()
            ) {
                // Estadísticas rápidas
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF1B4332).copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        FitzonQuickStat("Clases", "${classes.size}", greenLight)
                        FitzonQuickStat("Inscritos", "${classes.sumOf { it.enrolled }}", greenPrimary)
                        FitzonQuickStat("Llenas", "${classes.count { it.status == ClassStatus.FULL }}", Color(0xFFFF6B6B))
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = cardBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔍", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.White,
                                fontSize = 16.sp
                            ),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Buscar clase o coach...",
                                        color = textGray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("✕", fontSize = 16.sp, color = textGray)
                            }
                        }
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedDay == null,
                            onClick = { selectedDay = null },
                            label = { Text("Todos", fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = greenPrimary,
                                selectedLabelColor = Color(0xFF081C15),
                                containerColor = cardBackground,
                                labelColor = Color.White
                            )
                        )
                    }

                    items(days.size) { index ->
                        val day = days[index]
                        FilterChip(
                            selected = selectedDay == day,
                            onClick = { selectedDay = day },
                            label = { Text(day.name.take(3), fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = greenPrimary,
                                selectedLabelColor = Color(0xFF081C15),
                                containerColor = cardBackground,
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de clases agrupadas por día
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val groupedByDay = filteredClasses.groupBy { it.day }

                    groupedByDay.forEach { (day, classesByDay) ->
                        item {
                            Column {
                                Text(
                                    text = day,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    classesByDay.forEach { classSession ->
                                        ClassCard(
                                            classSession = classSession,
                                            cardBackground = cardBackground,
                                            greenPrimary = greenPrimary,
                                            greenLight = greenLight,
                                            textGray = textGray,
                                            onClick = {
                                                selectedClass = classSession
                                                showClassDetails = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Diálogo de detalles de la clase
    if (showClassDetails && selectedClass != null) {
        AlertDialog(
            onDismissRequest = { showClassDetails = false },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedClass!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = greenPrimary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = selectedClass!!.type,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = greenLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ClassDetailRow("📅", "Día", selectedClass!!.day)
                    Spacer(modifier = Modifier.height(12.dp))
                    ClassDetailRow("🕐", "Hora", selectedClass!!.time)
                    Spacer(modifier = Modifier.height(12.dp))
                    ClassDetailRow("👨‍🏫", "Coach", selectedClass!!.coach)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Capacidad",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = greenLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            progress = { selectedClass!!.enrolled.toFloat() / selectedClass!!.capacity },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp),
                            color = if (selectedClass!!.status == ClassStatus.FULL) Color(0xFFFF6B6B) else greenPrimary,
                            trackColor = Color(0xFF2D6A4F),
                            strokeCap = StrokeCap.Round
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${selectedClass!!.enrolled}/${selectedClass!!.capacity}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Ver inscritos */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = greenPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("👥 Ver Inscritos (${selectedClass!!.enrolled})", color = Color(0xFF081C15))
                        }

                        OutlinedButton(
                            onClick = { /* Editar */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = greenLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("✏️ Editar Clase")
                        }

                        if (selectedClass!!.status == ClassStatus.ACTIVE) {
                            OutlinedButton(
                                onClick = { /* Cancelar */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFFFB84D)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("🚫 Cancelar Clase")
                            }
                        } else if (selectedClass!!.status == ClassStatus.CANCELLED) {
                            OutlinedButton(
                                onClick = { /* Reactivar */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = greenLight
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("✅ Reactivar Clase")
                            }
                        }

                        OutlinedButton(
                            onClick = { /* Eliminar */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFFF6B6B)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("🗑️ Eliminar Clase")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showClassDetails = false }) {
                    Text("Cerrar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1B4332),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}

@Composable
fun ClassCard(
    classSession: ClassSession,
    cardBackground: Color,
    greenPrimary: Color,
    greenLight: Color,
    textGray: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
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
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1B4332)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🏋️", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classSession.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (classSession.status) {
                            ClassStatus.ACTIVE -> greenPrimary.copy(alpha = 0.2f)
                            ClassStatus.FULL -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                            ClassStatus.CANCELLED -> Color(0xFF5A5A5A)
                            ClassStatus.INACTIVE -> Color(0xFF5A5A5A)
                        }
                    ) {
                        Text(
                            text = when (classSession.status) {
                                ClassStatus.ACTIVE -> "Activa"
                                ClassStatus.FULL -> "Llena"
                                ClassStatus.CANCELLED -> "Cancelada"
                                ClassStatus.INACTIVE -> "Inactiva"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (classSession.status) {
                                ClassStatus.ACTIVE -> greenLight
                                ClassStatus.FULL -> Color(0xFFFF6B6B)
                                ClassStatus.CANCELLED -> Color(0xFFB7B7B7)
                                ClassStatus.INACTIVE -> Color(0xFFB7B7B7)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classSession.time,
                        fontSize = 13.sp,
                        color = greenLight,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", fontSize = 13.sp, color = textGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = classSession.duration,
                        fontSize = 13.sp,
                        color = textGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", fontSize = 13.sp, color = textGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = classSession.coach,
                        fontSize = 13.sp,
                        color = textGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Capacidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { classSession.enrolled.toFloat() / classSession.capacity },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp),
                        color = if (classSession.status == ClassStatus.FULL) Color(0xFFFF6B6B) else greenPrimary,
                        trackColor = Color(0xFF2D6A4F),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${classSession.enrolled}/${classSession.capacity}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (classSession.status == ClassStatus.FULL) Color(0xFFFF6B6B) else greenLight
                    )
                }
            }
        }
    }
}

@Composable
fun ClassDetailRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFFB7B7B7)
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ManageClassesScreenPreview() {
    ManageClassesScreen()
}