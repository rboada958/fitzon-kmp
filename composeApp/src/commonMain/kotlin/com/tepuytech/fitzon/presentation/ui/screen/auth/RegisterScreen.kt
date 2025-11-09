package com.tepuytech.fitzon.presentation.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.domain.enums.UserType
import com.tepuytech.fitzon.domain.model.auth.CustomRegisterRequest
import com.tepuytech.fitzon.domain.model.box.BoxesResponse
import com.tepuytech.fitzon.presentation.state.AuthUiState
import com.tepuytech.fitzon.presentation.state.BoxUiState
import com.tepuytech.fitzon.presentation.ui.composable.AthleteDashboardShimmer
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackgroundAlpha
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.athlete.AthleteDashboard
import com.tepuytech.fitzon.presentation.ui.screen.box.BoxDashboard
import com.tepuytech.fitzon.presentation.viewmodel.BoxViewModel
import com.tepuytech.fitzon.presentation.viewmodel.RegisterViewModel
import fitzon.composeapp.generated.resources.Res
import fitzon.composeapp.generated.resources.fitzon_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class Register : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<RegisterViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val boxViewModel = getScreenModel<BoxViewModel>()
        val boxUiState by boxViewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            boxViewModel.getBoxes()
        }

        when(boxUiState) {
            is BoxUiState.Loading -> {
                AthleteDashboardShimmer()
                return
            }
            is BoxUiState.SuccessBoxes -> {
                val boxState = (boxUiState as BoxUiState.SuccessBoxes).boxes
                RegisterScreen(
                    boxState = boxState,
                    onSignUpClick = {
                        println(it.toString())
                        viewModel.register(it)
                    },
                    onLoginClick = {
                        navigator.replaceAll(Login())
                    }
                )
            }
            is BoxUiState.Error -> {
                Text("Error: ${(boxUiState as BoxUiState.Error).message}")
            }

            else -> {}
        }

        LaunchedEffect(uiState) {
            if (uiState is AuthUiState.Success) {
                val userRoles = (uiState as AuthUiState.Success).user?.roles ?: emptyList()
                if ("ATHLETE" in userRoles) {
                    navigator.replaceAll(AthleteDashboard())
                } else {
                    navigator.replaceAll(BoxDashboard())
                }
            }
        }

        if (uiState is AuthUiState.Loading) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    boxState: List<BoxesResponse> = emptyList(),
    onSignUpClick: (CustomRegisterRequest) -> Unit,
    onLoginClick: () -> Unit
) {
    var selectedUserType by remember { mutableStateOf(UserType.PERSONA) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var boxName by remember { mutableStateOf("") }
    var boxAddress by remember { mutableStateOf("") }
    var boxPhone by remember { mutableStateOf("") }

    var selectedBox by remember { mutableStateOf("") }
    var selectedBoxId by remember { mutableStateOf("") }
    var expandedBoxDropdown by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isFormAthleteValid =
        email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                name.isNotEmpty() && selectedBox.isNotEmpty() && password == confirmPassword

    val isFormBoxValid =
        email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
                && boxName.isNotEmpty() && boxAddress.isNotEmpty() && boxPhone.isNotEmpty()

    if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
        errorMessage = "Las contraseñas no coinciden"
    } else if (errorMessage == "Las contraseñas no coinciden") {
        errorMessage = ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(Res.drawable.fitzon_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp).offset(y = 32.dp)
            )

            Text(
                text = "Fitzon",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Únete a la comunidad",
                fontSize = 14.sp,
                color = textGray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card de Registro
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBackgroundAlpha
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Selector de tipo de usuario
                    Text(
                        text = "Registrarse como:",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón PERSONA
                        OutlinedButton(
                            onClick = { selectedUserType = UserType.PERSONA },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedUserType == UserType.PERSONA)
                                    greenPrimary.copy(alpha = 0.3f)
                                else
                                    Color.Transparent,
                                contentColor = if (selectedUserType == UserType.PERSONA)
                                    greenLight
                                else
                                    textGray
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                brush = if (selectedUserType == UserType.PERSONA)
                                    Brush.linearGradient(listOf(greenLight, greenPrimary))
                                else
                                    Brush.linearGradient(listOf(Color(0xFF2D6A4F), Color(0xFF2D6A4F)))
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("🧑", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Atleta",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Botón BOX
                        OutlinedButton(
                            onClick = { selectedUserType = UserType.BOX },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedUserType == UserType.BOX)
                                    greenPrimary.copy(alpha = 0.3f)
                                else
                                    Color.Transparent,
                                contentColor = if (selectedUserType == UserType.BOX)
                                    greenLight
                                else
                                    textGray
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                brush = if (selectedUserType == UserType.BOX)
                                    Brush.linearGradient(listOf(greenLight, greenPrimary))
                                else
                                    Brush.linearGradient(listOf(Color(0xFF2D6A4F), Color(0xFF2D6A4F)))
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("🏋️", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "BOX",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campos comunes
                    Text(
                        text = if (selectedUserType == UserType.PERSONA) "Nombre Completo" else "Nombre del BOX",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = if (selectedUserType == UserType.PERSONA) name else boxName,
                        onValueChange = {
                            if (selectedUserType == UserType.PERSONA) name = it else boxName = it
                        },
                        placeholder = {
                            Text(
                                if (selectedUserType == UserType.PERSONA)
                                    "Juan Pérez"
                                else
                                    "CrossFit Central",
                                color = textGray
                            )
                        },
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

                    // Campos específicos según tipo de usuario
                    if (selectedUserType == UserType.BOX) {
                        // Dirección del BOX
                        Text(
                            text = "Dirección",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = boxAddress,
                            onValueChange = { boxAddress = it },
                            placeholder = {
                                Text("Calle Principal #123", color = textGray)
                            },
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

                        // Teléfono del BOX
                        Text(
                            text = "Teléfono",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = boxPhone,
                            onValueChange = { boxPhone = it },
                            placeholder = {
                                Text("+52 123 456 7890", color = textGray)
                            },
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        // Dropdown para seleccionar BOX (solo para PERSONA)
                        Text(
                            text = "BOX al que perteneces",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedBoxDropdown,
                            onExpandedChange = { expandedBoxDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = selectedBox,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = {
                                    Text("Selecciona tu BOX", color = textGray)
                                },
                                trailingIcon = {
                                    Text(
                                        text = if (expandedBoxDropdown) "▲" else "▼",
                                        color = textGray,
                                        fontSize = 12.sp
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp),
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
                                expanded = expandedBoxDropdown,
                                onDismissRequest = { expandedBoxDropdown = false },
                                modifier = Modifier.background(Color(0xFF1B4332))
                            ) {
                                boxState.forEach { box ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                box.name,
                                                color = Color.White,
                                                fontSize = 14.sp
                                            )
                                        },
                                        onClick = {
                                            selectedBox = box.name
                                            selectedBoxId = box.id
                                            expandedBoxDropdown = false
                                        },
                                        colors = MenuDefaults.itemColors(
                                            textColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Email
                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text("tu@email.com", color = textGray)
                        },
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
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña
                    Text(
                        text = "Contraseña",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text("••••••••", color = textGray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(
                                    text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                                    fontSize = 20.sp
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = greenLight,
                            unfocusedBorderColor = Color(0xFF2D6A4F),
                            focusedContainerColor = Color(0xFF0D2818),
                            unfocusedContainerColor = Color(0xFF0D2818)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmar Contraseña
                    Text(
                        text = "Confirmar Contraseña",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = {
                            Text("••••••••", color = textGray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Text(
                                    text = if (confirmPasswordVisible) "👁️" else "👁️‍🗨️",
                                    fontSize = 20.sp
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = greenLight,
                            unfocusedBorderColor = Color(0xFF2D6A4F),
                            focusedContainerColor = Color(0xFF0D2818),
                            unfocusedContainerColor = Color(0xFF0D2818)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = errorMessage,
                                fontSize = 14.sp,
                                color = Color(0xFFFF6B6B),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Registro
                    Button(
                        onClick = {

                            if (password != confirmPassword) {
                                return@Button
                            }

                            if (selectedUserType == UserType.PERSONA) {
                                if (name.isEmpty() || selectedBox.isEmpty()) {
                                    return@Button
                                }

                                val registerRequest = CustomRegisterRequest(
                                    password = password,
                                    email = email,
                                    role = "ATHLETE",
                                    name = name,
                                    boxId = selectedBoxId
                                )
                                onSignUpClick(registerRequest)
                            } else {
                                if (boxName.isEmpty() || boxAddress.isEmpty() || boxPhone.isEmpty()) {
                                    return@Button
                                }

                                val registerRequest = CustomRegisterRequest(
                                    password = password,
                                    email = email,
                                    role = "BOX_OWNER",
                                    boxName = boxName,
                                    location = boxAddress,
                                    phone = boxPhone
                                )
                                onSignUpClick(registerRequest)
                            }
                        },
                        enabled = if (selectedUserType == UserType.PERSONA) isFormAthleteValid else isFormBoxValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary,
                            disabledContainerColor = Color(0xFF2D6A4F)
                        )
                    ) {
                        Text(
                            text = "Crear Cuenta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Divider con "o"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF2D6A4F)
                        )
                        Text(
                            text = "o",
                            color = textGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF2D6A4F)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Link de Login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta? ",
                            color = textGray,
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = { onLoginClick() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                color = greenLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text de Footer
            Text(
                text = "Al registrarte, aceptas los Términos de Servicio y Política de Privacidad de Fitzon",
                fontSize = 12.sp,
                color = textGray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onSignUpClick = {},
        onLoginClick = {},
    )
}