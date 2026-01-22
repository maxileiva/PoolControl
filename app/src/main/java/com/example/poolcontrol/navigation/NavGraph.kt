package com.example.poolcontrol.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.poolcontrol.ui.components.appDrawer
import com.example.poolcontrol.ui.components.defaultDrawerItems
import kotlinx.coroutines.launch

@Composable

fun AppNavGraph(
    navController: NavController //libreria para manipular navegacion (controlar nav)
){
    //manejar el estado del drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //uso de corutina para manipular el cierre/apertura del drawer
    val scope = rememberCoroutineScope()

    //Helpers -> ayduan a manipular directamente las navegaciones
    val goLogin: () -> Unit = {navController.navigate(Route.Login.path)} //ir al login

    //contenedor principal para nuestro menu laterla

    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = { //contenido del menu
            appDrawer(
                items = defaultDrawerItems (
                    onLogin = {
                        scope.launch { drawerState.close() }
                        goLogin()
                    }
                )
            )
        }
    ) { }

}