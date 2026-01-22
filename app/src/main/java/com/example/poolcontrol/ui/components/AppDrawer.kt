package com.example.poolcontrol.ui.components

import android.text.LoginFilter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable

fun appDrawer (
    items: List<DrawerItem>, //lista con los items
    modifier: Modifier = Modifier //modificador del diseño
) {
    //crear ventana modal para el menu lateral desplegable
    ModalDrawerSheet(
        modifier = modifier
    ) {
        //dibujar todos los items del menu
        //recordando que vienen en una lista
        items.forEach { item ->            //guardar en la variable item cada elemento
                                            // que consiga en la lista  //funciones de " -> " ocurren momentaneamente
            NavigationDrawerItem (
                label = { Text(text = item.label) },
                selected = false,
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.label) },
                modifier = Modifier,
                colors = NavigationDrawerItemDefaults.colors()

                )
        }
    }
}

//funcion para rellenar la lista de items del menu

@Composable //para crear interfaz grafica composable

fun defaultDrawerItems (
    onLogin: () -> Unit
): List <DrawerItem> = listOf(
    DrawerItem("Ir al Login", Icons.Filled.Face, onLogin)
)