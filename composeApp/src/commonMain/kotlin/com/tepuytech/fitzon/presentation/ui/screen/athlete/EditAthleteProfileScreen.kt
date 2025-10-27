package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.tepuytech.fitzon.domain.model.athletes.AthleteProfileResponse
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.state.AthleteUiState
import com.tepuytech.fitzon.presentation.ui.composable.FitzonTopAppBar
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.viewmodel.AthleteViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class EditAthleteProfile(val profile: AthleteProfileResponse) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AthleteViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val cleanProfile = profile.copy(
            weight = profile.weight?.split(" ")?.firstOrNull(),
            height = profile.height?.split(" ")?.firstOrNull()
        )
        var formData by remember { mutableStateOf(cleanProfile) }
        var showSuccessDialog by remember { mutableStateOf(false) }

        LaunchedEffect(uiState) {
            when (uiState) {
                is AthleteUiState.UpdateSuccess -> {
                    val updatedProfile = (uiState as AthleteUiState.UpdateSuccess).update
                    formData = formData.copy(
                        age = updatedProfile.age,
                        weight = "${updatedProfile.weight}",
                        height = "${updatedProfile.height}"
                    )
                    showSuccessDialog = true
                }

                is AthleteUiState.Error -> {}

                else -> Unit
            }
        }

        EditAthleteProfileScreen(
            profile = formData,
            onBackClick = {
                navigator.pop()
            },
            updateProfileOnClick = { currentForm ->
                val age = currentForm.age ?: profile.age ?: 0

                val weight = currentForm.weight
                    ?.takeIf { it.isNotBlank() }
                    ?.toDoubleOrNull()
                    ?: profile.weight?.toDoubleOrNull()
                    ?: 0.0

                val height = currentForm.height
                    ?.takeIf { it.isNotBlank() }
                    ?.toDoubleOrNull()
                    ?: profile.height?.toDoubleOrNull()
                    ?: 0.0

                viewModel.updateAthleteProfile(age, weight, height)
            }
        )

        if (uiState is AthleteUiState.Loading) {
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
                                ProfileUpdateRow("⚖️", "Peso", "${formData.weight} kg")
                                Spacer(modifier = Modifier.height(8.dp))
                                ProfileUpdateRow("📏", "Altura", "${formData.height} m")
                                Spacer(modifier = Modifier.height(8.dp))
                                ProfileUpdateRow("🎂", "Edad", "${formData.age} años")
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
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAthleteProfileScreen(
    profile: AthleteProfileResponse = AthleteProfileResponse(),
    onBackClick: () -> Unit = {},
    updateProfileOnClick: (AthleteProfileResponse) -> Unit = {}
) {

    var formData by remember { mutableStateOf(profile) }

    val isFormValid = !formData.name.isNullOrEmpty() &&
            !formData.email.isNullOrEmpty() &&
            !formData.weight.isNullOrEmpty() &&
            !formData.height.isNullOrEmpty() &&
            (formData.age ?: 0) > 0 &&
            !formData.boxName.isNullOrEmpty()

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

                // Información Personal
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
                    value = formData.name ?: "Juan Pérez",
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
                    value = formData.email ?: "john.mclean@examplepetstore.com",
                    onValueChange = {},
                    enabled = false,
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
                        unfocusedContainerColor = Color(0xFF0D2818),
                        disabledTextColor = Color.White,
                        disabledContainerColor = Color(0xFF0D2818),
                        disabledBorderColor = Color(0xFF2D6A4F)
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

                OutlinedTextField(
                    value = formData.boxName ?: "Box",
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    placeholder = { Text("Box", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = greenLight,
                        unfocusedBorderColor = Color(0xFF2D6A4F),
                        focusedContainerColor = Color(0xFF0D2818),
                        unfocusedContainerColor = Color(0xFF0D2818),
                        disabledTextColor = Color.White,
                        disabledContainerColor = Color(0xFF0D2818),
                        disabledBorderColor = Color(0xFF2D6A4F)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Datos Físicos
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
                            value = formData.weight ?: "",
                            onValueChange = { formData = formData.copy(weight = it) },
                            placeholder = { Text("75 kg", color = textGray) },
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
                            value = formData.height ?: "",
                            onValueChange = { formData = formData.copy(height = it) },
                            placeholder = { Text("1.75 m", color = textGray) },
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
                    value = formData.age?.toString() ?: "",
                    onValueChange = { input ->
                        val digitsOnly = input.filter { it.isDigit() }
                        val newAge = digitsOnly.toIntOrNull()
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
                                "La información personal no se pueden cambiar",
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
                        onClick = { onBackClick() },
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
                        onClick = { updateProfileOnClick(formData) },
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