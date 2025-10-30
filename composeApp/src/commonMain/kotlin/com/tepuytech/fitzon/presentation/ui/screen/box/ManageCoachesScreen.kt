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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.coaches
import com.tepuytech.fitzon.domain.enums.CoachStatus
import com.tepuytech.fitzon.domain.model.Coach
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.FitzonQuickStat
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class ManageCoaches: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageCoachesScreen(
            onBackClick = {
                navigator.pop()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCoachesScreen(
    onBackClick: () -> Unit = {}
) {

    var coaches by remember {
        mutableStateOf(
            coaches
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedCoach by remember { mutableStateOf<Coach?>(null) }
    var showCoachDetails by remember { mutableStateOf(false) }

    val filters = listOf("Todos", "Activos", "Inactivos", "Licencia")

    val filteredCoaches = coaches.filter { coach ->
        val matchesSearch = coach.name.contains(searchQuery, ignoreCase = true) ||
                coach.email.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Activos" -> coach.status == CoachStatus.ACTIVE
            "Inactivos" -> coach.status == CoachStatus.INACTIVE
            "Licencia" -> coach.status == CoachStatus.ON_LEAVE
            else -> true
        }
        matchesSearch && matchesFilter
    }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestionar Coaches",
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
                onClick = { /* Contratar nuevo coachName */ },
                containerColor = greenPrimary,
                contentColor = Color(0xFF081C15)
            ) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contratar Coach", fontWeight = FontWeight.Bold)
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
                        FitzonQuickStat("Total", "${coaches.size}", greenLight)
                        FitzonQuickStat("Activos", "${coaches.count { it.status == CoachStatus.ACTIVE }}", greenPrimary)
                        FitzonQuickStat("Clases/sem", "${coaches.sumOf { it.classesPerWeek }}", Color(0xFFFFB84D))
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
                                        "Buscar coachName por nombre...",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 13.sp) },
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

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCoaches) { coach ->
                        CoachCard(
                            coach = coach,
                            cardBackground = cardBackground,
                            greenPrimary = greenPrimary,
                            greenLight = greenLight,
                            textGray = textGray,
                            onClick = {
                                selectedCoach = coach
                                showCoachDetails = true
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Diálogo de detalles del coachName
    if (showCoachDetails && selectedCoach != null) {
        AlertDialog(
            onDismissRequest = { showCoachDetails = false },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = greenPrimary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("👨‍🏫", fontSize = 30.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = selectedCoach!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("⭐", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${selectedCoach!!.rating}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenLight
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CoachDetailRow("📧", "Email", selectedCoach!!.email)
                    Spacer(modifier = Modifier.height(12.dp))
                    CoachDetailRow("📱", "Teléfono", selectedCoach!!.phone)
                    Spacer(modifier = Modifier.height(12.dp))
                    CoachDetailRow("📅", "Miembro desde", selectedCoach!!.joinDate)
                    Spacer(modifier = Modifier.height(12.dp))
                    CoachDetailRow("⏱️", "Experiencia", selectedCoach!!.experience)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Especializaciones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = greenLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedCoach!!.specialties.forEach { specialty ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = greenPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = specialty,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = greenLight,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Certificaciones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = greenLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        selectedCoach!!.certifications.forEach { cert ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✓", fontSize = 16.sp, color = greenPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cert,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {  },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = greenPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("📅 Ver Horario", color = Color(0xFF081C15))
                        }

                        if (selectedCoach!!.status == CoachStatus.ACTIVE) {
                            OutlinedButton(
                                onClick = { /* Cambiar a licencia */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFFFB84D)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("🚪 Marcar en Licencia")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { /* Reactivar */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = greenLight
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("✅ Reactivar")
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
                            Text("🗑️ Eliminar Coach")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCoachDetails = false }) {
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
fun CoachCard(
    coach: Coach,
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
            // Avatar con status
            Box {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = when (coach.status) {
                        CoachStatus.ACTIVE -> greenPrimary.copy(alpha = 0.3f)
                        CoachStatus.INACTIVE -> Color(0xFF5A5A5A)
                        CoachStatus.ON_LEAVE -> Color(0xFFFFB84D).copy(alpha = 0.3f)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👨‍🏫", fontSize = 24.sp)
                    }
                }

                // Status indicator
                Surface(
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.BottomEnd),
                    shape = CircleShape,
                    color = when (coach.status) {
                        CoachStatus.ACTIVE -> greenPrimary
                        CoachStatus.INACTIVE -> Color(0xFF5A5A5A)
                        CoachStatus.ON_LEAVE -> Color(0xFFFFB84D)
                    }
                ) {}
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
                        text = coach.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", fontSize = 14.sp)
                        Text(
                            text = "${coach.rating}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = coach.specialties.take(2).joinToString(", "),
                    fontSize = 13.sp,
                    color = textGray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📅 ${coach.classesPerWeek} clases",
                        fontSize = 12.sp,
                        color = textGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", fontSize = 12.sp, color = textGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "👥 ${coach.totalStudents}",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }
            }

            // Status badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (coach.status) {
                    CoachStatus.ACTIVE -> greenPrimary.copy(alpha = 0.2f)
                    CoachStatus.INACTIVE -> Color(0xFF5A5A5A)
                    CoachStatus.ON_LEAVE -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                }
            ) {
                Text(
                    text = when (coach.status) {
                        CoachStatus.ACTIVE -> "Activo"
                        CoachStatus.INACTIVE -> "Inactivo"
                        CoachStatus.ON_LEAVE -> "Licencia"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (coach.status) {
                        CoachStatus.ACTIVE -> greenLight
                        CoachStatus.INACTIVE -> Color(0xFFB7B7B7)
                        CoachStatus.ON_LEAVE -> Color(0xFFFFB84D)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CoachDetailRow(
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
fun ManageCoachesScreenPreview() {
    ManageCoachesScreen()
}