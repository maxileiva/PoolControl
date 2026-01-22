package com.example.poolcontrol.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poolcontrol.ui.components.appDrawer
import com.example.poolcontrol.ui.components.defaultDrawerItems
import com.example.poolcontrol.ui.screen.AddReserva
import com.example.poolcontrol.ui.screen.ConfirmarReserva
import com.example.poolcontrol.ui.screen.DashboardAdmin
import com.example.poolcontrol.ui.screen.DashboardCliente
import com.example.poolcontrol.ui.screen.LoginScreen
import com.example.poolcontrol.ui.screen.RegisterScreen
import com.example.poolcontrol.ui.screen.ConsultaReserva
import com.example.poolcontrol.ui.screen.VerPerfil
import kotlinx.coroutines.launch


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        //helpers de navegacion
        val goRegister: () -> Unit = { navController.navigate(Route.Register.path) } //ir al Registro
        val goLogin:() -> Unit = { navController.navigate(Route.Login.path) }
        val goReserva:() -> Unit = { navController.navigate(Route.AddReserva.path) }
        val goPerfil:() -> Unit = { navController.navigate(Route.Perfil.path) }
        val goConsultaReserva:() -> Unit = { navController.navigate(Route.Consultar.path) }
        val goDashAdmin:() -> Unit = {navController.navigate(Route.Admin.path)}
        val goDashCli:() -> Unit = {navController.navigate(Route.Home.path)}
        val goConfirmaReserva:() -> Unit = {navController.navigate(Route.AddReserva.path)}

        // LOGIN
        composable(Route.Login.path) {
            LoginScreen(
                onGoRegister = goRegister,
                onLoginClick = { email ->
                    if (email.contains("admin", ignoreCase = true)) {
                        goDashAdmin()
                    } else {
                        goDashCli()
                    }
                }
            )
        }
        composable(Route.Register.path) {
            RegisterScreen(
                onGoLogin = { navController.popBackStack() },
                onRegisterClick = { email ->
                    if (email.contains("admin", ignoreCase = true)) {
                        goDashAdmin()
                    } else {
                        goDashCli()
                    }
                }
            )
        }

        composable(Route.Home.path) {
            DashboardCliente(
                onGoAddReserva = goReserva,
                onGoPerfil = goPerfil,
                onGoConsultaReserva = goConsultaReserva
            )
        }

        composable(Route.Admin.path) {
            DashboardAdmin(
                onGoDashboardAdmin = goDashAdmin,
                onGoAddReserva = goReserva,
                onGoPerfil = goPerfil,
                onGoConsultaReserva = goConsultaReserva

            )
        }

        composable(Route.Confirmar.path) { backStackEntry ->
            // Extraemos el valor "fecha" que definimos en la clase Route
            val fechaArg = backStackEntry.arguments?.getString("fecha") ?: "Sin fecha"

            ConfirmarReserva(
                fechaSeleccionada = fechaArg,
                onBack = { navController.popBackStack() },
                onConfirmar = {
                    // Después de confirmar, mejor volver al Home o Admin en lugar de AddReserva
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Consultar.path) {
            ConsultaReserva(onBack = {
                navController.popBackStack()
            })
        }

        composable(Route.Perfil.path) {
            VerPerfil (
                onBack = {navController.popBackStack()},
                onLogout = goLogin
            )
        }
    }
}
