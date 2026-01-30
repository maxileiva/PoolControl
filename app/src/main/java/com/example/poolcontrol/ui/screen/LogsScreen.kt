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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poolcontrol.data.remote.dto.LogDTO
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.components.BottomAppBar
import com.example.poolcontrol.ui.viewmodel.LogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    onGoHome: () -> Unit,
    onGoPerfil: () -> Unit,
    onGoConsultaReserva: () -> Unit,
    onGoLogs: () -> Unit,
    logViewModel: LogViewModel = viewModel()
) {

    val logs by logViewModel.logs.collectAsState()
    val loading by logViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        logViewModel.cargarLogs()
    }

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = {
            BottomAppBar(
                onGoHome = onGoHome,
                onGoPerfil = onGoPerfil,
                onGoConsultaReserva = onGoConsultaReserva,
                onGoLogs = onGoLogs
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                logs.isEmpty() -> {
                    Text(
                        text = "No hay logs disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(logs) { log ->
                            LogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogItem(log: LogDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = log.nivel,
                fontWeight = FontWeight.Bold,
                color = when (log.nivel.uppercase()) {
                    "ERROR" -> Color.Red
                    "WARN" -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            Text(text = log.mensaje)
            Text(
                text = "Servicio: ${log.servicio}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = log.fecha.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
