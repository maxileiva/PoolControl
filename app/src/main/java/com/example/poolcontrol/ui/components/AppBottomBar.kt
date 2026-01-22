package com.example.poolcontrol.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun BottomAppBar(
                 onGoAddReserva: () -> Unit,
                 onGoPerfil: () -> Unit,
                 onGoConsultaReserva: () -> Unit,
                 onGoDashboardAdmin: () -> Unit
) {
    androidx.compose.material3.BottomAppBar {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, // Distribuye espacio igual entre ellos
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = { onGoAddReserva() }) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Añadir")
            }
            IconButton(onClick = { onGoDashboardAdmin()}) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
            IconButton(onClick = { onGoConsultaReserva() }) {
                Icon(Icons.Filled.Menu, contentDescription = "Consultar Reservas")
            }
            IconButton(onClick = { onGoPerfil()}) {
                Icon(Icons.Filled.Face, contentDescription = "Perfil")
            }
        }
}
}