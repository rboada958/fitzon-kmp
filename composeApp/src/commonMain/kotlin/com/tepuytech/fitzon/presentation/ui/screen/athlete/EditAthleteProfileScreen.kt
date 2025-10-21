package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.tepuytech.fitzon.data.availableBoxes
import com.tepuytech.fitzon.data.profile
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.FitzonTopAppBar
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import org.jetbrains.compose.ui.tooling.preview.Preview

class EditAthleteProfile : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        EditAthleteProfileScreen(
            onBackClick = {
                navigator.pop()
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAthleteProfileScreen(
    onBackClick: () -> Unit = {}
) {

    var formData by remember {
        mutableStateOf(
            profile
        )
    }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var expandedBox by remember { mutableStateOf(false) }

    val isFormValid = formData.name.isNotEmpty() &&
            formData.email.isNotEmpty() &&
            formData.weight.isNotEmpty() &&
            formData.height.isNotEmpty() &&
            formData.age.toString().isNotEmpty() &&
            formData.boxName.isNotEmpty()

    val platform = getPlatform()

    Scaffold(
        topBar = {
            FitzonTopAppBar(
                title = "Editar Perfil",
                onBackClick = onBackClick,
                platform = platform,
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
                // Avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = greenPrimary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("👤", fontSize = 50.sp)
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.BottomEnd),
                        shape = CircleShape,
                        color = greenLight
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("📷", fontSize = 18.sp)
                        }
                    }
                }

                // Sección: Información Personal
                Text(
                    text = "Información Personal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Nombre
                Text(
                    text = "Nombre Completo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = formData.name,
                    onValueChange = { formData = formData.copy(name = it) },
                    placeholder = { Text("Juan Pérez", color = textGray) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = formData.email,
                    onValueChange = { formData = formData.copy(email = it) },
                    placeholder = { Text("tu@email.com", color = textGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

                Spacer(modifier = Modifier.height(16.dp))

                // BOX
                Text(
                    text = "Mi BOX",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedBox,
                    onExpandedChange = { expandedBox = it }
                ) {
                    OutlinedTextField(
                        value = formData.boxName,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona workout", color = textGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Text(
                                text = if (expandedBox) "▲" else "▼",
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
                        expanded = expandedBox,
                        onDismissRequest = { expandedBox = false },
                        modifier = Modifier.background(Color(0xFF1B4332))
                    ) {

                        availableBoxes.forEach { box ->
                            DropdownMenuItem(
                                text = {
                                   Text(box, color = Color.White, fontSize = 14.sp)
                                },
                                onClick = {
                                    formData = formData.copy(boxName = box)
                                    expandedBox = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Datos Físicos
                Text(
                    text = "Datos Físicos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Row: Peso y Altura
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Peso
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Peso (kg)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = formData.weight,
                            onValueChange = { formData = formData.copy(weight = it) },
                            placeholder = { Text("75", color = textGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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

                    // Altura
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Altura (m)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = formData.height,
                            onValueChange = { formData = formData.copy(height = it) },
                            placeholder = { Text("1.75", color = textGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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

                Spacer(modifier = Modifier.height(16.dp))

                // Edad
                Text(
                    text = "Edad",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = formData.age.toString(),
                    onValueChange = {
                        val newAge = it.toIntOrNull() ?: 0
                        formData = formData.copy(age = newAge)
                    },
                    placeholder = { Text("28", color = textGray) },
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

                Spacer(modifier = Modifier.height(32.dp))

                // Información de cambios
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
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Nota",
                                fontSize = 12.sp,
                                color = Color(0xFFFFB84D),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "El email solo se puede cambiar desde configuración por seguridad",
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Cancelar */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = greenLight
                        )
                    ) {
                        Text(
                            "Cancelar",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
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
                            "✓ Guardar Cambios",
                            color = if (isFormValid) Color(0xFF081C15) else textGray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Text(
                    text = "✓ Perfil Actualizado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text("Tus cambios han sido guardados exitosamente", fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0D2818)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            ProfileUpdateRow("👤", "Nombre", formData.name)
                            Spacer(modifier = Modifier.height(8.dp))
                            ProfileUpdateRow("⚖️", "Peso", "${formData.weight} kg")
                            Spacer(modifier = Modifier.height(8.dp))
                            ProfileUpdateRow("📏", "Altura", "${formData.height} m")
                            Spacer(modifier = Modifier.height(8.dp))
                            ProfileUpdateRow("🎂", "Edad", "${formData.age} años")
                            Spacer(modifier = Modifier.height(8.dp))
                            ProfileUpdateRow("🏋️", "BOX", formData.boxName)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
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
fun ProfileUpdateRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFFB7B7B7)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF74C69D)
            )
        }
    }
}

@Preview
@Composable
fun EditAthleteProfileScreenPreview() {
    EditAthleteProfileScreen()
}