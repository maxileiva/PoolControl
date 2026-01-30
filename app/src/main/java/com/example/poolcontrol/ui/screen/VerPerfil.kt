package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerPerfil(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val usuario = authViewModel.userLogueado
    val bg = MaterialTheme.colorScheme.surfaceVariant

    // Estados de edición de perfil
    var editando by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var apellido by remember { mutableStateOf(usuario?.apellido ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }

    // Estados para el Dialog de contraseña
    var mostrarDialogPass by remember { mutableStateOf(false) }
    var passActual by remember { mutableStateOf("") }
    var passNueva by remember { mutableStateOf("") }
    var confirmarPass by remember { mutableStateOf("") }

    // Regex y Validaciones
    val soloLetrasRegex = Regex("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]*$")
    val nombreValido = nombre.isNotBlank() && nombre.length >= 2
    val apellidoValido = apellido.isNotBlank() && apellido.length >= 2
    val telefonoValido = telefono.length == 9
    val formularioValido = nombreValido && apellidoValido && telefonoValido

    // Validación de cambio de contraseña
    val contrasenasCoinciden = passNueva == confirmarPass && passNueva.isNotBlank()
    val largoMinPass = passNueva.length >= 6

    // --- DIALOGO DE CAMBIO DE CONTRASEÑA ---
    if (mostrarDialogPass) {
        AlertDialog(
            onDismissRequest = { mostrarDialogPass = false },
            title = { Text("Seguridad de la Cuenta", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Ingresa los datos para actualizar tu clave.", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = passActual,
                        onValueChange = { passActual = it },
                        label = { Text("Contraseña Actual") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = passNueva,
                        onValueChange = { passNueva = it },
                        label = { Text("Nueva Contraseña (min. 6)") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = passNueva.isNotEmpty() && !largoMinPass,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmarPass,
                        onValueChange = { confirmarPass = it },
                        label = { Text("Confirmar Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = confirmarPass.isNotEmpty() && !contrasenasCoinciden,
                        singleLine = true,
                        supportingText = {
                            if (confirmarPass.isNotEmpty() && !contrasenasCoinciden) {
                                Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.cambiarContrasenaRemoto(passActual, passNueva) { exito, msj ->
                            Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
                            if (exito) {
                                mostrarDialogPass = false
                                passActual = ""; passNueva = ""; confirmarPass = ""
                            }
                        }
                    },
                    enabled = contrasenasCoinciden && largoMinPass && passActual.isNotBlank()
                ) { Text("Actualizar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogPass = false
                    passActual = ""; passNueva = ""; confirmarPass = ""
                }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Circular
            Surface(
                modifier = Modifier.size(100.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (editando) "Editando Perfil" else "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (!editando) {
                        ProfileItem(label = "Nombre", value = "${usuario?.nombre} ${usuario?.apellido}", icon = Icons.Default.Person)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                        ProfileItem(label = "Correo", value = usuario?.correo ?: "", icon = Icons.Default.Email)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                        ProfileItem(label = "Teléfono", value = usuario?.telefono ?: "No registrado", icon = Icons.Default.Phone)
                    } else {
                        // Edición: Nombre
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { if (it.matches(soloLetrasRegex)) nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = !nombreValido,
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Edición: Apellido
                        OutlinedTextField(
                            value = apellido,
                            onValueChange = { if (it.matches(soloLetrasRegex)) apellido = it },
                            label = { Text("Apellido") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = !apellidoValido,
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Edición: Teléfono
                        OutlinedTextField(
                            value = telefono,
                            onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 9) telefono = it },
                            label = { Text("Teléfono (9 dígitos)") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = !telefonoValido && telefono.isNotEmpty(),
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción
            if (!editando) {
                Button(
                    onClick = { editando = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editar Datos")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { mostrarDialogPass = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.Lock, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar Contraseña")
                }
            } else {
                Button(
                    onClick = {
                        authViewModel.actualizarDatosRemotos(nombre, apellido, telefono) { exito ->
                            if (exito) {
                                editando = false
                                Toast.makeText(context, "Perfil Actualizado", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = formularioValido,
                    colors = ButtonDefaults.buttonColors(containerColor = if (formularioValido) Color(0xFF4CAF50) else Color.Gray)
                ) {
                    Text("Confirmar Cambios")
                }
                TextButton(
                    onClick = {
                        editando = false
                        nombre = usuario?.nombre ?: ""
                        apellido = usuario?.apellido ?: ""
                        telefono = usuario?.telefono ?: ""
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancelar", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}