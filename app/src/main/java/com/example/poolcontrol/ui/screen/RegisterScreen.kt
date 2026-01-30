package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    usuariosViewModel: UsuariosViewModel,
    onNavigateToLogin: () -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    val context = LocalContext.current
    val uiState = usuariosViewModel.uiState

    val soloLetrasRegex = Regex("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*$")
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val correoInvalido = correo.isNotEmpty() && !correo.matches(emailRegex)

    val bg = MaterialTheme.colorScheme.surfaceVariant

    LaunchedEffect(Unit) {
        usuariosViewModel.limpiarEstado()
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            usuariosViewModel.limpiarEstado()
            onNavigateToLogin()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            usuariosViewModel.limpiarEstado()
        }
    }

    val formularioValido =
        nombre.length in 2..50 &&
                apellido.length >= 2 &&
                correo.matches(emailRegex) &&
                telefono.length == 9 &&
                contrasena.length >= 6 &&
                contrasena == confirmarContrasena

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "Nueva Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text("Regístrate para continuar", color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            if (it.matches(soloLetrasRegex) && it.length <= 50) {
                                nombre = it
                            }
                        },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            if (it.matches(soloLetrasRegex)) {
                                apellido = it
                            }
                        },
                        label = { Text("Apellido") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it.trim() },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = correoInvalido,
                        supportingText = {
                            if (correoInvalido) {
                                Text(
                                    "Formato inválido",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() } && it.length <= 9) {
                                telefono = it
                            }
                        },
                        label = { Text("Teléfono (9 dígitos)") },
                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = { confirmarContrasena = it },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = { Icon(Icons.Default.CheckCircle, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = confirmarContrasena.isNotEmpty() && contrasena != confirmarContrasena,
                        supportingText = {
                            if (confirmarContrasena.isNotEmpty() && contrasena != confirmarContrasena) {
                                Text(
                                    text = "Las contraseñas no coinciden",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!uiState.isLoading) {
                        usuariosViewModel.registrar(
                            nombre = nombre,
                            apellido = apellido,
                            correo = correo,
                            telefono = telefono,
                            password = contrasena
                        )
                    }
                },
                enabled = formularioValido && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("REGISTRARSE", fontWeight = FontWeight.Bold)
                }
            }

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}
