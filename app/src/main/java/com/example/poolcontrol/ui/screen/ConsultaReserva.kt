package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.viewmodel.AuthViewModel
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaReserva(
    reservaViewModel: ReservaViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val user = authViewModel.userLogueado
    val esAdmin = user?.rolId == 1

    var reservaAEliminar by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        if (esAdmin) reservaViewModel.cargarTodasLasReservas()
        else user?.id?.let { reservaViewModel.cargarReservasPorCliente(it) }
    }

    val lista = if (esAdmin)
        reservaViewModel.todasLasReservas
    else
        reservaViewModel.reservasCliente

    Scaffold(topBar = { AppTopBar() }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            HeaderSeccion(
                titulo = if (esAdmin) "ADMINISTRACIÓN" else "MIS RESERVAS",
                onBack = onBack
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista) { reserva ->
                    ReservaCard(
                        reserva = reserva,
                        esAdmin = esAdmin,
                        onAprobar = {
                            reservaViewModel.actualizarEstadoReserva(
                                reserva.id!!,
                                "PAGADO",
                                true
                            )
                        },
                        onSolicitarCancelar = {
                            reservaViewModel.actualizarEstadoReserva(
                                reserva.id!!,
                                "SOLICITA CANCELACION",
                                false,
                                user?.id ?: 0
                            )
                        },
                        onEliminarClick = {
                            reservaAEliminar = reserva.id
                        },
                        onDecidirCancelacion = { aceptada, nota ->
                            reservaViewModel.decidirCancelacion(
                                reserva.id!!,
                                aceptada,
                                nota,
                                esAdmin,
                                reserva,
                                user?.id ?: 0
                            )
                        }
                    )
                }
            }
        }

        /* 🔴 DIÁLOGO DE CONFIRMACIÓN DE ELIMINAR */
        if (reservaAEliminar != null) {
            AlertDialog(
                onDismissRequest = { reservaAEliminar = null },
                title = {
                    Text("Confirmar eliminación")
                },
                text = {
                    Text("¿Estás seguro de que deseas eliminar esta reserva? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            reservaAEliminar?.let { id ->
                                reservaViewModel.eliminarReserva(id)
                                reservaViewModel.cargarTodasLasReservas()
                            }
                            reservaAEliminar = null
                        }
                    ) {
                        Text("ELIMINAR", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { reservaAEliminar = null }) {
                        Text("CANCELAR")
                    }
                }
            )
        }
    }
}

/* ========================================================= */

@Composable
fun ReservaCard(
    reserva: com.example.poolcontrol.data.network.ReservaDto,
    esAdmin: Boolean,
    onAprobar: () -> Unit,
    onSolicitarCancelar: () -> Unit,
    onEliminarClick: () -> Unit,
    onDecidirCancelacion: (Boolean, String) -> Unit
) {
    var notaAdmin by remember { mutableStateOf("") }

    val (colorFondo, textoEstado, colorTexto) = when (reserva.estado) {
        "PAGADO" -> Triple(Color(0xFFE8F5E9), "CONFIRMADA", Color(0xFF2E7D32))
        "SOLICITA CANCELACION" -> Triple(
            Color(0xFFFFEBEE),
            "PENDIENTE CANCELACIÓN",
            Color(0xFFC62828)
        )

        "CANCELACION RECHAZADA" -> Triple(
            Color(0xFFFFF3E0),
            "CANCELACIÓN RECHAZADA",
            Color(0xFFE65100)
        )

        "RECHAZO APROBADO" -> Triple(Color(0xFFF3E5F5), "CANCELADA (SIN ABONO)", Color(0xFF7B1FA2))
        else -> Triple(Color(0xFFFFF9C4), "PENDIENTE PAGO", Color(0xFFF57F17))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(reserva.nombrePiscina, fontWeight = FontWeight.Bold)

            Surface(color = colorFondo, shape = MaterialTheme.shapes.extraSmall) {
                Text(
                    textoEstado,
                    color = colorTexto,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Text("Fecha: ${reserva.fecha}", fontSize = 12.sp)

            val partes = reserva.detalles.split(" | NOTA ADMIN: ")
            Text(partes[0], fontSize = 12.sp, color = Color.Gray)

            if (partes.size > 1) {
                Text(
                    "NOTA ADMIN: ${partes[1]}",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (esAdmin) {

                if (reserva.estado == "SOLICITA CANCELACION") {
                    OutlinedTextField(
                        value = notaAdmin,
                        onValueChange = { notaAdmin = it },
                        label = { Text("Escriba el motivo aquí...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Button(
                            onClick = { onDecidirCancelacion(false, notaAdmin) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Rechazar", fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { onDecidirCancelacion(true, notaAdmin) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Aprobar Rechazo", fontSize = 11.sp)
                        }
                    }
                }

                if (reserva.estado == "PENDIENTE") {
                    Button(
                        onClick = onAprobar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("APROBAR PAGO")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = onEliminarClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar reserva",
                        tint = Color.Red
                    )
                }

            } else {
                // 👇 LÓGICA DEL CLIENTE (ESTO FALTABA)
                val puedePedirCancelacion =
                    reserva.estado == "PAGADO" || reserva.estado == "PENDIENTE"

                if (puedePedirCancelacion) {
                    OutlinedButton(
                        onClick = onSolicitarCancelar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("SOLICITAR CANCELACIÓN")
                    }
                } else if (reserva.estado == "CANCELACION RECHAZADA") {
                    Text(
                        text = "Esta reserva no puede volver a cancelarse.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

/* ========================================================= */

@Composable
fun HeaderSeccion(titulo: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
        Text(
            titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
