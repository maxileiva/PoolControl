package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(authViewModel: AuthViewModel, onNavigateToLogin: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    val context = LocalContext.current
    val soloLetrasRegex = Regex("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*$")
    val bg = MaterialTheme.colorScheme.surfaceVariant

    val formularioValido = nombre.length >= 2 && apellido.length >= 2 &&
            correo.contains("@") && telefono.length == 9 &&
            contrasena.length >= 6 && contrasena == confirmarContrasena

    Box(modifier = Modifier.fillMaxSize().background(bg)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text("Nueva Cuenta", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Regístrate para continuar", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nombre, onValueChange = { if (it.matches(soloLetrasRegex)) nombre = it },
                        label = { Text("Nombre") }, leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = apellido, onValueChange = { if (it.matches(soloLetrasRegex)) apellido = it },
                        label = { Text("Apellido") }, leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = correo, onValueChange = { correo = it.trim() },
                        label = { Text("Correo Electrónico") }, leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = telefono, onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 9) telefono = it },
                        label = { Text("Teléfono (9 dígitos)") }, leadingIcon = { Icon(Icons.Default.Phone, null) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = contrasena, onValueChange = { contrasena = it },
                        label = { Text("Contraseña") }, leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = confirmarContrasena, onValueChange = { confirmarContrasena = it },
                        label = { Text("Confirmar") }, leadingIcon = { Icon(Icons.Default.CheckCircle, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        isError = confirmarContrasena.isNotEmpty() && contrasena != confirmarContrasena
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.registrar(nombre, apellido, correo, contrasena, telefono) { exito, mensaje ->
                        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                        if (exito) {
                            authViewModel.limpiarCampos()
                            onNavigateToLogin()
                        }
                    }
                },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("REGISTRARSE", fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = { authViewModel.limpiarCampos(); onNavigateToLogin() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}