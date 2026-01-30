package com.example.poolcontrol.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomAppBar(
    onGoHome: () -> Unit,
    onGoPerfil: () -> Unit,
    onGoConsultaReserva: () -> Unit,
    onGoLogs: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onGoHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onGoConsultaReserva,
            icon = { Icon(Icons.Default.List, contentDescription = "Reservas") },
            label = { Text("Reservas") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onGoLogs,
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Logs") },
            label = { Text("Logs") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onGoPerfil,
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}
