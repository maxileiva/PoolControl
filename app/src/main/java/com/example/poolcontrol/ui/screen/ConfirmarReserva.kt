package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.R
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.domain.validation.validateNameLettersOnly
import com.example.poolcontrol.domain.validation.validatePhoneDigitsOnly
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarReserva(
    fechaSeleccionada: String,
    piscinaId: Int,
    esAdmin: Boolean,
    reservaViewModel: ReservaViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onConfirmar: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    val context = LocalContext.current

    // ESTADOS
    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cantidadPersonas by remember { mutableStateOf("") }

    // AUTO-RELLENO DESDE AUTH VIEW MODEL
    LaunchedEffect(authViewModel.userLogueado) {
        if (!esAdmin && authViewModel.userLogueado != null) {
            val u = authViewModel.userLogueado
            nombreCompleto = "${u?.nombre} ${u?.apellido}"
            telefono = u?.numero ?: ""
        }
    }

    // VALIDACIONES
    val errorNombre = validateNameLettersOnly(nombreCompleto)
    val errorTelefono = validatePhoneDigitsOnly(telefono)
    val errorCantidad = when {
        cantidadPersonas.isBlank() -> "Ingrese cantidad"
        cantidadPersonas.toIntOrNull() == null -> "Solo números"
        cantidadPersonas.toInt() < 10 -> "Mínimo 10 personas"
        cantidadPersonas.toInt() > 200 -> "Máximo 200 personas"
        else -> null
    }

    val formularioValido = errorNombre == null && errorTelefono == null &&
            errorCantidad == null && nombreCompleto.isNotBlank()

    val imagenPiscina = if (piscinaId == 2) R.drawable.piscina2 else R.drawable.piscina1
    val nombrePiscina = if (piscinaId == 2) "Piscina Recreativa" else "Piscina Olímpica"

    Scaffold(topBar = { AppTopBar() }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(bg).padding(innerPadding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("DETALLES DE RESERVA", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF616161))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = imagenPiscina),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(nombrePiscina, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Fecha: $fechaSeleccionada", color = Color.LightGray)
                    }
                }
            }

            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { if (esAdmin) nombreCompleto = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !esAdmin,
                isError = errorNombre != null
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { if (esAdmin) telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !esAdmin,
                isError = errorTelefono != null
            )

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = { cantidadPersonas = it },
                label = { Text("Cantidad (10 - 200)") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorCantidad != null,
                supportingText = { errorCantidad?.let { Text(it) } }
            )

            Button(
                onClick = {
                    reservaViewModel.realizarReserva(
                        fecha = fechaSeleccionada,
                        nombre = nombreCompleto,
                        telefono = telefono,
                        cantidad = cantidadPersonas.toInt(),
                        piscinaId = piscinaId,
                        userId = authViewModel.userLogueado?.id ?: 1L,
                        onSuccess = {
                            Toast.makeText(context, "Reserva Exitosa", Toast.LENGTH_SHORT).show()
                            onConfirmar() //creamos mensaje emergente
                        },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("FINALIZAR RESERVA", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("VOLVER")
            }
        }
    }
}