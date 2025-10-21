package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.boxInfo
import com.tepuytech.fitzon.domain.model.CoachInfo
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackground
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class BoxInfoAthlete : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        BoxInfoAthleteScreen(
            onBackClick = {
                navigator.pop()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxInfoAthleteScreen(
    onBackClick: () -> Unit = {}
) {
    var userRating by remember { mutableStateOf(0) }
    var showRatingDialog by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val platform = getPlatform()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Información del BOX",
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
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Text("📤", fontSize = 24.sp)
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
            ) {
                // Header con logo y nombre
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF1B4332)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = CircleShape,
                                color = greenPrimary,
                                border = BorderStroke(4.dp, greenLight)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🏋️", fontSize = 50.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = boxInfo.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Rating display
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                repeat(5) { index ->
                                    Text(
                                        text = if (index < boxInfo.rating.toInt()) "★" else "☆",
                                        fontSize = 30.sp,
                                        modifier = Modifier.offset(y = (-3).dp),
                                        color = if (index < boxInfo.rating.toInt()) Color(0xFFFFB84D) else textGray
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${boxInfo.rating}",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "${boxInfo.totalReviews} valoraciones • ${boxInfo.totalMembers} miembros",
                                fontSize = 14.sp,
                                color = textGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { showRatingDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                )
                            ) {
                                Text(
                                    "⭐ Valorar mi Experiencia",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF081C15),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Descripción
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 100)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Acerca de",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Text(
                                text = boxInfo.description,
                                fontSize = 15.sp,
                                color = Color.White,
                                lineHeight = 22.sp,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Galería de fotos
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 150)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Galería",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            boxInfo.photos.forEach { photo ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    color = cardBackground
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(photo, fontSize = 40.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Coaches
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 200)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "👨‍🏫 Entrenadores (${boxInfo.coaches.size})",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            boxInfo.coaches.forEach { coach ->
                                CoachCard(
                                    coach = coach,
                                    cardBackground = cardBackground,
                                    textGray = textGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Amenidades
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 250)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Instalaciones",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                boxInfo.amenities.chunked(2).forEach { row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        row.forEach { amenity ->
                                            Row(
                                                modifier = Modifier.weight(1f),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = amenity,
                                                    fontSize = 14.sp,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                        if (row.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                    if (row != boxInfo.amenities.chunked(2).last()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información de contacto
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500, delayMillis = 300)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Contacto y Ubicación",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = cardBackground
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                ContactInfoRow("📍", "Dirección", boxInfo.address)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                ContactInfoRow("📱", "Teléfono", boxInfo.phone)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                ContactInfoRow("📧", "Email", boxInfo.email)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFF1B4332)
                                )
                                ContactInfoRow("🕐", "Horario", boxInfo.schedule)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Llamar */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = greenLight
                                ),
                                border = BorderStroke(2.dp, greenLight)
                            ) {
                                Text("📞 Llamar", modifier = Modifier.padding(vertical = 4.dp))
                            }

                            OutlinedButton(
                                onClick = { /* Abrir mapa */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = greenLight
                                ),
                                border = BorderStroke(2.dp, greenLight)
                            ) {
                                Text("🗺️ Mapa", modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (showRatingDialog) {
        AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = {
                Text(
                    text = "Valora tu Experiencia",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Cómo ha sido tu experiencia en ${boxInfo.name}?",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = textGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { userRating = index + 1 }
                            ) {
                                Text(
                                    text = if (index < userRating) "★" else "☆",
                                    fontSize = 35.sp,
                                    color = if (index < userRating) Color(0xFFFFB84D) else textGray
                                )
                            }
                        }
                    }

                    if (userRating > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (userRating) {
                                1 -> "Muy malo"
                                2 -> "Malo"
                                3 -> "Regular"
                                4 -> "Bueno"
                                5 -> "Excelente"
                                else -> ""
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = greenLight
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Guardar valoración
                        showRatingDialog = false
                    },
                    enabled = userRating > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = greenPrimary,
                        disabledContainerColor = Color(0xFF2D6A4F)
                    )
                ) {
                    Text("Enviar", color = Color(0xFF081C15))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRatingDialog = false
                    userRating = 0
                }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1B4332),
            titleContentColor = Color.White,
            textContentColor = textGray
        )
    }
}

@Composable
fun CoachCard(
    coach: CoachInfo,
    cardBackground: Color,
    textGray: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0xFF1B4332)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(coach.icon, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = coach.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = coach.specialty,
                    fontSize = 14.sp,
                    color = textGray
                )
            }
        }
    }
}

@Composable
fun ContactInfoRow(
    icon: String,
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF74C69D)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            color = Color.White,
            lineHeight = 20.sp,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}

@Preview
@Composable
fun BoxInfoAthleteScreenPreview() {
    BoxInfoAthleteScreen()
}