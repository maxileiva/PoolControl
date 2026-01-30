package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.components.BottomAppBar
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAdmin(
    reservaViewModel: ReservaViewModel,
    onGoAddReserva: () -> Unit,
    onGoPerfil: () -> Unit,
    onGoHome: () -> Unit,
    onGoConsultaReserva: () -> Unit,
    onGoLogs: () -> Unit
) {

    LaunchedEffect(Unit) {
        reservaViewModel.cargarTodasLasReservas()
    }

    val listaReservas = reservaViewModel.todasLasReservas
    val hoyStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val reservaHoy = listaReservas.find { it.fecha == hoyStr }
    val proximasReservas = listaReservas.filter { it.fecha > hoyStr }
        .sortedBy { it.fecha }

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = {
            BottomAppBar(
                onGoHome = onGoHome,
                onGoPerfil = onGoPerfil,
                onGoConsultaReserva = onGoConsultaReserva,
                onGoLogs = onGoLogs
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoAddReserva,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Agregar reserva",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reserva de Hoy ($hoyStr)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (reservaHoy != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Piscina: ${reservaHoy.nombrePiscina}", fontWeight = FontWeight.Bold)
                        Text("Detalles: ${reservaHoy.detalles}")
                        Text("Monto: $${reservaHoy.precio}", color = Color(0xFF2E7D32))
                        Text("Estado: ${reservaHoy.estado}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            } else {
                Text("No hay reservas para hoy", color = Color.Gray)
            }

            Divider()

            Text("Agenda Próxima", fontWeight = FontWeight.Bold)

            if (proximasReservas.isEmpty()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No hay reservas futuras", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(proximasReservas) { reserva ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(formatearFechaParaMostrar(reserva.fecha), fontWeight = FontWeight.Bold)
                                    Text(reserva.nombrePiscina)
                                    Text(reserva.detalles, maxLines = 1)
                                }
                                Badge {
                                    Text(if (reserva.nombrePiscina.contains("Olímpica")) "P1" else "P2")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatearFechaParaMostrar(fechaDb: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("EEEE d 'de' MMMM", Locale.getDefault())
        val date = parser.parse(fechaDb)
        date?.let { formatter.format(it) } ?: fechaDb
    } catch (e: Exception) {
        fechaDb
    }
}
