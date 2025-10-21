package com.tepuytech.fitzon.presentation.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.data.availableBoxes
import com.tepuytech.fitzon.domain.enums.UserType
import com.tepuytech.fitzon.getPlatform
import com.tepuytech.fitzon.presentation.ui.composable.backgroundGradient
import com.tepuytech.fitzon.presentation.ui.composable.cardBackgroundAlpha
import com.tepuytech.fitzon.presentation.ui.composable.greenLight
import com.tepuytech.fitzon.presentation.ui.composable.greenPrimary
import com.tepuytech.fitzon.presentation.ui.composable.textGray
import com.tepuytech.fitzon.presentation.ui.screen.athlete.AthleteDashboard
import com.tepuytech.fitzon.presentation.ui.screen.box.BoxDashboard
import fitzon.composeapp.generated.resources.Res
import fitzon.composeapp.generated.resources.fitzon_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class Register : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val platform = getPlatform()

        RegisterScreen(
            onSignUpClick = {
                if (platform.name.contains("iOS")) {
                    navigator.replaceAll(BoxDashboard())
                } else {
                    navigator.replaceAll(AthleteDashboard())
                }
            },
            onLoginClick = {
                navigator.replaceAll(Login())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSignUpClick: () -> Unit,
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
    var expandedBoxDropdown by remember { mutableStateOf(false) }



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
                                availableBoxes.forEach { box ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                box,
                                                color = Color.White,
                                                fontSize = 14.sp
                                            )
                                        },
                                        onClick = {
                                            selectedBox = box
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Registro
                    Button(
                        onClick = { onSignUpClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
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

            // Texto de Footer
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
        onLoginClick = {}
    )
}