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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@Composable
fun VerPerfil(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val usuario = authViewModel.userLogueado
    val bg = MaterialTheme.colorScheme.surfaceVariant

    // Estados para la edición
    var editando by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var apellido by remember { mutableStateOf(usuario?.apellido ?: "") }
    var telefono by remember { mutableStateOf(usuario?.numero ?: "") }

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
            // Avatar
            Surface(
                modifier = Modifier.size(100.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    Icons.Default.Person, null,
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (!editando) {
                        // VISTA DE LECTURA
                        ProfileItem(label = "Nombre", value = "${usuario?.nombre} ${usuario?.apellido}", icon = Icons.Default.Person)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileItem(label = "Correo", value = usuario?.email ?: "", icon = Icons.Default.Email)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileItem(label = "Teléfono", value = usuario?.numero ?: "", icon = Icons.Default.Phone)
                    } else {
                        // VISTA DE EDICIÓN
                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Lógica de botones
            if (!editando) {
                Button(
                    onClick = { editando = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editar Datos")
                }
            } else {
                Button(
                    onClick = {
                        // 'nombre', 'apellido' y 'telefono' son los 'remember' de tus TextFields
                        authViewModel.actualizarPerfil(nombre, apellido, telefono) { exito ->
                            if (exito) {
                                Toast.makeText(context, "¡Cambios guardados!", Toast.LENGTH_SHORT).show()
                                editando = false // Solo cerramos la edición si se guardó en la BD
                            } else {
                                Toast.makeText(context, "Error: No se pudo actualizar", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Confirmar Cambios")
                }
                TextButton(onClick = { editando = false }) { Text("Cancelar") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Volver") }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) { Text("Cerrar Sesión") }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, icon: ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}