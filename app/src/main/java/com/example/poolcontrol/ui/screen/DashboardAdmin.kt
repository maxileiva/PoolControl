package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
    reservaViewModel: ReservaViewModel, // Agregado el ViewModel
    onGoAddReserva: () -> Unit,
    onGoPerfil: () -> Unit,
    onGoConsultaReserva: () -> Unit,
    onGoDashboardAdmin: () -> Unit
) {
    // 1. Cargar datos al iniciar
    LaunchedEffect(Unit) {
        reservaViewModel.cargarTodasLasReservas()
    }

    val listaReservas = reservaViewModel.todasLasReservas
    val hoyStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // 2. Filtrar reserva de hoy y próximas
    val reservaHoy = listaReservas.find { it.fecha == hoyStr }
    val proximasReservas = listaReservas.filter { it.fecha > hoyStr }
        .sortedBy { it.fecha } // Ordenar por fecha más cercana

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = {
            BottomAppBar(
                onGoAddReserva = onGoAddReserva,
                onGoPerfil = onGoPerfil,
                onGoConsultaReserva = onGoConsultaReserva,
                onGoDashboardAdmin = onGoDashboardAdmin
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onGoAddReserva,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Añadir Reserva") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- SECCIÓN RESERVA DE HOY ---
            Text(
                text = "Reserva de Hoy ($hoyStr)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (reservaHoy != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Cliente: ${reservaHoy.nombreCliente}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = "Teléfono: ${reservaHoy.telefonoCliente}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Personas: ${reservaHoy.cant_personas}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Piscina ID: ${reservaHoy.piscinaId}", color = Color.Gray)
                    }
                }
            } else {
                Text(
                    text = "No hay reservas para hoy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Divider()

            // --- SECCIÓN PRÓXIMOS CLIENTES ---
            Text(
                text = "Agenda de Próximos Clientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (proximasReservas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay reservas futuras", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(proximasReservas) { reserva ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = formatearFechaParaMostrar(reserva.fecha),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(text = "Cliente: ${reserva.nombreCliente}")
                                    Text(text = "${reserva.cant_personas} personas", style = MaterialTheme.typography.bodySmall)
                                }
                                Badge(containerColor = if(reserva.piscinaId == 1) Color.Blue else Color.Magenta) {
                                    Text("P${reserva.piscinaId}", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
            // Espacio para que el FAB no tape el contenido
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// Función auxiliar para que la fecha se vea bonita (ej: 24 Jan)
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