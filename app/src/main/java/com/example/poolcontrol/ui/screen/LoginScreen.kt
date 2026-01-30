package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(authViewModel: AuthViewModel, onGoRegister: () -> Unit, onLoginClick: (String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // --- COLORES EXACTOS DEL REGISTER ---
    val bg = MaterialTheme.colorScheme.surfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        authViewModel.limpiarCampos()
    }

    if (showDialog) {
        var c by remember { mutableStateOf("") }
        var t by remember { mutableStateOf("") }
        var p by remember { mutableStateOf("") }
        var errorDialog by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Recuperar Contraseña", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = c, onValueChange = { c = it; errorDialog = null },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = t,
                        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 9) t = it },
                        label = { Text("Teléfono (9 dígitos)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = p, onValueChange = { p = it; errorDialog = null },
                        label = { Text("Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorDialog?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (t.length != 9) errorDialog = "El teléfono debe tener 9 dígitos"
                    else {
                        authViewModel.recuperarPassword(c, t, p) { exito, msj ->
                            if (exito) {
                                Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                                showDialog = false
                            } else errorDialog = "Credenciales no coinciden"
                        }
                    }
                }) { Text("Confirmar") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
        )
    }

    // FONDO: surfaceVariant (Gris suave del Register)
    Box(modifier = Modifier.fillMaxSize().background(bg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // LOGO: Igual al Register pero con la imagen del login
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Image(
                    painter = painterResource(id = com.example.poolcontrol.R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("PoolControl", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = primaryColor)
            Text("Regístrate para continuar", color = Color.Gray)

            Spacer(modifier = Modifier.height(30.dp))

            // CARD: Blanca, igual a la del Register
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = authViewModel.email,
                        onValueChange = {
                            authViewModel.email = it
                            authViewModel.mensajeError = null
                        },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = authViewModel.mensajeError != null,
                        supportingText = {
                            authViewModel.mensajeError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )


                    OutlinedTextField(
                        value = authViewModel.password,
                        onValueChange = {
                            authViewModel.password = it
                            authViewModel.mensajeError = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = authViewModel.mensajeError != null,
                        supportingText = {
                            authViewModel.mensajeError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    TextButton(onClick = { showDialog = true }, modifier = Modifier.align(Alignment.End)) {
                        Text("¿Olvidaste tu contraseña?", fontSize = 13.sp)
                    }
                }
            }

            // MENSAJE DE ERROR
            authViewModel.mensajeError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // BOTÓN: Color primario del sistema
            Button(
                onClick = { onLoginClick(authViewModel.email, authViewModel.password) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = authViewModel.loginValido,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("INGRESAR", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes una cuenta?", color = Color.Gray)
                TextButton(onClick = onGoRegister) {
                    Text("Regístrate aquí", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}