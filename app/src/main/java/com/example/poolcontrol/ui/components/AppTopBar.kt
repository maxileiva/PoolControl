package com.example.poolcontrol.ui.components

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun AppTopBar(
  onOpenDrawer: () -> Unit, //abre menu desplegable
  onHome: () -> Unit,
  onLogin: () -> Unit,
  onRegister: () -> Unit,
  onAddReserva: () -> Unit
){
    //variable que recuerde el estado
    //del menu desplegable o del menu 3 puntos

    var verMenutrespuntos by remember { mutableStateOf(false) }

    //barra alineada centro del topbar

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = "Pool Control",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1, //cant lineas mostrar el texto
                overflow = TextOverflow.Ellipsis //agrega ... si no se puede mostrar el texto completo

            )
        },
        //icono hamburgesa para menu desplegable
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(imageVector = Icons.Filled.Menu,"menu")
            }
        },

        actions = {
            IconButton(onClick = onLogin) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Login")
            }

            IconButton(onClick = {verMenutrespuntos = true}) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Ver mas")
            }

            DropdownMenu(
                expanded = verMenutrespuntos,
                onDismissRequest = { verMenutrespuntos = false}
            ) { }

        }

    )
}