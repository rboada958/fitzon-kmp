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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.tepuytech.fitzon.domain.model.member.MemberResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.MemberUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.FitzonQuickStat
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.viewmodel.MemberViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class ManageMembers() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MemberViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getMembers()
        }

        when(uiState) {
            is MemberUiState.Loading -> {
                AthleteDashboardShimmer()
                return
            }
            is MemberUiState.Success -> {
                val membersState = (uiState as MemberUiState.Success).members
                ManageMembersScreen(
                    membersState = membersState,
                    onBackClick = { navigator.pop() }
                )
            }
            is MemberUiState.Error -> {
                Text("Error: ${(uiState as MemberUiState.Error).message}")
            }

            else -> {}
        }



    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageMembersScreen(
    membersState: List<MemberResponse> = emptyList(),
    onBackClick: () -> Unit = {},
    onPromoteClick: (String) -> Unit = {}
) {
    var members by remember {
        mutableStateOf(
            membersState
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedMember by remember { mutableStateOf<MemberResponse?>(null) }
    var showMemberDetails by remember { mutableStateOf(false) }

    val filters = listOf("Todos", "Activos", "Inactivos", "Pendientes")

    val filteredMembers = members.filter { member ->
        val matchesSearch = member.name.contains(searchQuery, ignoreCase = true) ||
                member.email.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Activos" -> member.status == "ACTIVE"
            "Inactivos" -> member.status == "INACTIVE"
            "Pendientes" -> member.status == "PENDING"
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
                        text = "Gestionar Miembros",
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
                modifier = Modifier.fillMaxSize()
            ) {
                // Estadísticas
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
                        FitzonQuickStat("Total", "${members.size}", greenLight)
                        FitzonQuickStat("Activos", "${members.count { it.status == "ACTIVE" }}", greenPrimary)
                        FitzonQuickStat("Pendientes", "${members.count { it.paymentStatus == "PENDING" }}", Color(0xFFFF6B6B))
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
                                        "Buscar por nombre o email...",
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

                // Filters
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

                // Lista de miembros
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (filteredMembers.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = "👨‍🏫",
                                        fontSize = 48.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    Text(
                                        text = "Sin miembros",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No hay miembros con los filtros aplicados",
                                        fontSize = 14.sp,
                                        color = textGray,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }else {
                        items(filteredMembers) { member ->
                            MemberCard(
                                member = member,
                                cardBackground = cardBackground,
                                greenPrimary = greenPrimary,
                                greenLight = greenLight,
                                textGray = textGray,
                                onClick = {
                                    selectedMember = member
                                    showMemberDetails = true
                                }
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

    // Diálogo de detalles del miembro
    if (showMemberDetails && selectedMember != null) {
        AlertDialog(
            onDismissRequest = { showMemberDetails = false },
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
                            Text("👤", fontSize = 30.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = selectedMember!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MemberDetailRow("📧", "Email", selectedMember!!.email)
                    Spacer(modifier = Modifier.height(12.dp))
                    MemberDetailRow("📱", "Teléfono", selectedMember!!.phone ?: "N/A")
                    Spacer(modifier = Modifier.height(12.dp))
                    MemberDetailRow("💳", "Membresía", selectedMember!!.membershipType)
                    Spacer(modifier = Modifier.height(12.dp))
                    MemberDetailRow("📅", "Miembro desde", selectedMember!!.joinedAt)
                    Spacer(modifier = Modifier.height(12.dp))
                    MemberDetailRow("🏋️", "Workouts", "${selectedMember!!.totalWorkouts}")

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = greenLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("📊 Ver Historial")
                        }

                        if (selectedMember!!.paymentStatus != "PAID") {
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFFFB84D)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("💰 Recordar Pago")
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                onPromoteClick(selectedMember!!.id)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFFF6B6B)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("🗑️ Promover a coach")
                        }

                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFFF6B6B)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("🗑️ Eliminar Miembro")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showMemberDetails = false }) {
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
fun MemberCard(
    member: MemberResponse,
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
            // Avatar
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = when (member.status) {
                    "ACTIVE" -> greenPrimary.copy(alpha = 0.3f)
                    "INACTIVE" -> Color(0xFF5A5A5A)
                    "PENDING" -> Color(0xFFFFB84D).copy(alpha = 0.3f)
                    else -> Color(0xFF5A5A5A)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 24.sp)
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
                        text = member.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (member.status) {
                            "ACTIVE" -> greenPrimary.copy(alpha = 0.2f)
                            "INACTIVE" -> Color(0xFF5A5A5A)
                            "PENDING" -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                            else -> Color(0xFF5A5A5A)
                        }
                    ) {
                        Text(
                            text = when (member.status) {
                                "ACTIVE" -> "Activo"
                                "INACTIVE" -> "Inactivo"
                                "PENDING" -> "Pendiente"
                                else -> ""
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (member.status) {
                                "ACTIVE" -> greenLight
                                "INACTIVE" -> Color(0xFFB7B7B7)
                                "PENDING" -> Color(0xFFFFB84D)
                                else -> Color.Unspecified
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = member.email,
                    fontSize = 13.sp,
                    color = textGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💳 ${member.membershipType}",
                        fontSize = 12.sp,
                        color = textGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", fontSize = 12.sp, color = textGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🏋️ ${member.totalWorkouts} workouts",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }
            }

            // Payment status indicator
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = when (member.paymentStatus) {
                    "PAID" -> greenPrimary.copy(alpha = 0.2f)
                    "PENDING" -> Color(0xFFFFB84D).copy(alpha = 0.2f)
                    "OVERDUE" -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                    else -> Color(0xFF5A5A5A)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when (member.paymentStatus) {
                            "PAID" -> "✓"
                            "PENDING" -> "⏰"
                            "OVERDUE" -> "⚠️"
                            else -> ""
                        },
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MemberDetailRow(
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
fun ManageMembersPreview() {
    ManageMembersScreen()
}