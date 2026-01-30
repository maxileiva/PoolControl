package com.example.poolcontrol.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.viewmodel.AuthViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerPerfil(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val usuario = authViewModel.userLogueado

    // --- ESTADOS ---
    var editando by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var apellido by remember { mutableStateOf(usuario?.apellido ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }
    var errorServidorTelefono by remember { mutableStateOf<String?>(null) }

    var mostrarDialogPass by remember { mutableStateOf(false) }
    var passActual by remember { mutableStateOf("") }
    var passNueva by remember { mutableStateOf("") }
    var confirmarPass by remember { mutableStateOf("") }

    // 🔹 NUEVO (solo agregado)
    var mostrarOpcionesFoto by remember { mutableStateOf(false) }

    // --- VALIDACIÓN ---
    val formularioValido = nombre.length >= 2 && apellido.length >= 2 && telefono.length == 9

    val bitmapPerfil = remember(usuario?.fotoPerfil) {
        usuario?.fotoPerfil?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }

    // --- CÁMARA (EXISTENTE, NO TOCADA) ---
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { btmp ->
            btmp?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)

                authViewModel.actualizarPerfilCompleto(
                    nombre,
                    apellido,
                    telefono,
                    base64
                ) { _, msj ->
                    Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
                }
            }
        }

    // --- GALERÍA (NUEVO, SIN CAMBIAR VARIABLES) ---
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)

                authViewModel.actualizarPerfilCompleto(
                    nombre,
                    apellido,
                    telefono,
                    base64
                ) { _, msj ->
                    Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
                }
            }
        }

    // --- DIÁLOGO FOTO (NUEVO) ---
    if (mostrarOpcionesFoto) {
        AlertDialog(
            onDismissRequest = { mostrarOpcionesFoto = false },
            title = { Text("Cambiar foto de perfil") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            mostrarOpcionesFoto = false
                            cameraLauncher.launch(null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Tomar foto")
                    }

                    Button(
                        onClick = {
                            mostrarOpcionesFoto = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Photo, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Elegir de galería")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarOpcionesFoto = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --- DIÁLOGO CONTRASEÑA (EXISTENTE) ---
    if (mostrarDialogPass) {
        AlertDialog(
            onDismissRequest = { mostrarDialogPass = false },
            title = { Text("Cambiar Contraseña") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = passActual,
                        onValueChange = { passActual = it },
                        label = { Text("Contraseña Actual") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = passNueva,
                        onValueChange = { passNueva = it },
                        label = { Text("Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmarPass,
                        onValueChange = { confirmarPass = it },
                        label = { Text("Confirmar Nueva") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
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
                                passActual = ""
                                passNueva = ""
                                confirmarPass = ""
                            }
                        }
                    },
                    enabled = passNueva == confirmarPass && passNueva.length >= 6
                ) {
                    Text("Cambiar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogPass = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(topBar = { AppTopBar() }) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO (SOLO CAMBIO: ahora abre opciones)
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { mostrarOpcionesFoto = true }
                ) {
                    if (bitmapPerfil != null) {
                        Image(
                            bitmap = bitmapPerfil.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, null, modifier = Modifier.padding(30.dp))
                    }
                }
                Icon(
                    Icons.Default.Edit,
                    null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- RESTO DEL CÓDIGO NO SE TOCA ---
            if (!editando) {
                Text(
                    "${usuario?.nombre} ${usuario?.apellido}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(usuario?.correo ?: "", color = Color.Gray)
                Text(usuario?.telefono ?: "Sin teléfono", color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        nombre = usuario?.nombre ?: ""
                        apellido = usuario?.apellido ?: ""
                        telefono = usuario?.telefono ?: ""
                        editando = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editar Perfil")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { mostrarDialogPass = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Lock, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar Contraseña")
                }
            } else {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } && it.length <= 9) {
                            telefono = it
                            errorServidorTelefono = null
                        }
                    },
                    label = { Text("Teléfono (9 dígitos)") },
                    isError = errorServidorTelefono != null,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.actualizarPerfilCompleto(nombre, apellido, telefono, null) {
                                exito, msj ->
                            Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
                            if (exito) editando = false
                        }
                    },
                    enabled = formularioValido,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Guardar Cambios")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider()

            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Logout, null, tint = Color.Red)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión", color = Color.Red)
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}
