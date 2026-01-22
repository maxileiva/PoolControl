package com.example.poolcontrol.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.poolcontrol.ui.screen.AddReserva
import com.example.poolcontrol.ui.screen.ConfirmarReserva
import com.example.poolcontrol.ui.screen.DashboardAdmin
import com.example.poolcontrol.ui.screen.DashboardCliente
import com.example.poolcontrol.ui.screen.LoginScreen
import com.example.poolcontrol.ui.screen.RegisterScreen
import com.example.poolcontrol.ui.screen.ConsultaReserva
import com.example.poolcontrol.ui.screen.VerPerfil
import com.example.poolcontrol.ui.viewmodel.AuthViewModel


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
            // Obtenemos el ViewModel
            val authViewModel: AuthViewModel = viewModel ()

            LoginScreen(
                onGoRegister = goRegister,
                authViewModel = authViewModel,
                onLoginClick = { email ->

                    val cleanEmail = email.trim()

                    if (cleanEmail.contains("admin", ignoreCase = true)) {
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

        composable(Route.AddReserva.path) {
            val vieneDeAdmin = navController.previousBackStackEntry?.destination?.route == Route.Admin.path

            AddReserva(
                navController = navController,
                onBack = { navController.popBackStack() },
                esAdmin = vieneDeAdmin
            )
        }

        composable(
            route = "ConfirmarReserva/{fecha}/{esAdmin}", // Ruta actualizada
            arguments = listOf(
                navArgument("fecha") { type = NavType.StringType },
                navArgument("esAdmin") { type = NavType.BoolType } // Nuevo argumento booleano
            )
        ) { backStackEntry ->
            val fechaArg = backStackEntry.arguments?.getString("fecha") ?: "Sin fecha"
            val esAdmin = backStackEntry.arguments?.getBoolean("esAdmin") ?: false

            ConfirmarReserva(
                fechaSeleccionada = fechaArg,
                onBack = { navController.popBackStack() },
                onConfirmar = {
                    val destino = if (esAdmin) Route.Admin.path else Route.Home.path
                    navController.navigate(destino) {
                        popUpTo(destino) { inclusive = true }
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
