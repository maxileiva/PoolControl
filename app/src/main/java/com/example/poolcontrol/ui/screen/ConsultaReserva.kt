package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    // Obtenemos el usuario logueado para filtrar
    val usuario = authViewModel.userLogueado
    val esAdmin = usuario?.rolId == 1

    // Cargamos las reservas al entrar
    LaunchedEffect(Unit) {
        reservaViewModel.cargarTodasLasReservas()
    }

    // Filtramos la lista: Admin ve todo, Cliente solo lo suyo por su ID
    val listaFiltrada = if (esAdmin) {
        reservaViewModel.todasLasReservas
    } else {
        reservaViewModel.todasLasReservas.filter { it.userId == usuario?.id }
    }

    Scaffold(
        topBar = { AppTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (esAdmin) "Panel de Control de Reservas" else "Mis Reservas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (listaFiltrada.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No hay reservas para mostrar", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listaFiltrada) { reserva ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (reserva.piscinaId == 1) Color(0xFFE3F2FD) else Color(0xFFF3E5F5)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Fecha: ${reserva.fecha}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(text = "Cliente: ${reserva.nombreCliente}")
                                    Text(text = "Tel: ${reserva.telefonoCliente}", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = "Piscina: ${if(reserva.piscinaId == 1) "Olímpica" else "Recreativa"}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                // Botón de eliminar solo visible para el Admin
                                if (esAdmin) {
                                    IconButton(onClick = { reservaViewModel.eliminarReserva(reserva) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Borrar",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Volver")
            }
        }
    }
}