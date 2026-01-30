package com.example.poolcontrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poolcontrol.R
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.domain.validation.validateNameLettersOnly
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
    val precioPorPersona = 8000.0

    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cantidadPersonas by remember { mutableStateOf("") }

    val totalReserva = (cantidadPersonas.toIntOrNull() ?: 0) * precioPorPersona

    LaunchedEffect(authViewModel.userLogueado) {
        if (!esAdmin) {
            authViewModel.userLogueado?.let { u ->
                nombreCompleto = "${u.nombre} ${u.apellido ?: ""}"
                telefono = u.telefono?.take(9) ?: ""
            }
        }
    }

    val errorNombre = validateNameLettersOnly(nombreCompleto)
    val errorTelefono = if (telefono.length != 9) "Requeridos 9 dígitos" else null
    val errorCantidad = when {
        cantidadPersonas.isEmpty() -> "Ingrese cantidad"
        (cantidadPersonas.toIntOrNull() ?: 0) < 10 -> "Mínimo 10"
        (cantidadPersonas.toIntOrNull() ?: 0) > 200 -> "Máximo 200"
        else -> null
    }

    val formularioValido = errorNombre == null && errorTelefono == null &&
            errorCantidad == null && nombreCompleto.isNotEmpty()

    val nombrePiscina = if (piscinaId == 2) "Piscina Recreativa" else "Piscina Olímpica"
    val imagenRes = if (piscinaId == 2) R.drawable.piscina2 else R.drawable.piscina1

    Scaffold(topBar = { AppTopBar() }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("RESUMEN DE RESERVA", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF616161))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = imagenRes),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(nombrePiscina, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text("Fecha: $fechaSeleccionada", color = Color.LightGray)
                        Text("Precio p/p: $8.000", color = Color(0xFF81C784), fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { if (esAdmin) nombreCompleto = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !esAdmin,
                isError = errorNombre != null,
                supportingText = { errorNombre?.let { Text(it) } }
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    if (esAdmin && it.all { c -> c.isDigit() } && it.length <= 9) {
                        telefono = it
                    }
                },
                label = { Text("Teléfono (9 dígitos)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !esAdmin,
                isError = errorTelefono != null,
                supportingText = {
                    errorTelefono?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = { if (it.length <= 3) cantidadPersonas = it },
                label = { Text("Cantidad de personas") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorCantidad != null,
                supportingText = { Text("Mínimo 10 - Máximo 200") }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TOTAL A PAGAR:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "$${String.format("%,.0f", totalReserva)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            Button(
                onClick = {
                    val idClienteActual = if (esAdmin) -1L else (authViewModel.userLogueado?.id ?: -1L)
                    val estadoInicial = if (esAdmin) "PAGADO" else "PENDIENTE"

                    val detallesFinales = if (esAdmin) {
                        "VENTA ADMIN: $nombreCompleto | Tel: $telefono | Cant: $cantidadPersonas"
                    } else {
                        "CLIENTE: $nombreCompleto | Cant: $cantidadPersonas"
                    }

                    // LLAMADA AL VIEWMODEL (Asegúrate que coincida con la definición en ReservaViewModel)
                    reservaViewModel.realizarReserva(
                        fecha = fechaSeleccionada,
                        nombrePiscina = nombrePiscina,
                        detalles = detallesFinales,
                        precio = totalReserva,
                        idCliente = idClienteActual,
                        estado = estadoInicial,
                        onSuccess = {
                            Toast.makeText(context, "Reserva confirmada exitosamente", Toast.LENGTH_LONG).show()
                            onConfirmar()
                        },
                        onError = { error ->
                            android.util.Log.e("RESERVA_ERROR", error)
                            Toast.makeText(context, "Error al reservar: $error", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("FINALIZAR RESERVA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("VOLVER")
            }
        }
    }
}