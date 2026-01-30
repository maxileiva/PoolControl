package com.example.poolcontrol.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.poolcontrol.ui.screen.*
import com.example.poolcontrol.ui.viewmodel.AuthViewModel
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import com.example.poolcontrol.ui.viewmodel.UsuariosViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val usuariosViewModel: UsuariosViewModel = viewModel()

    val reservaViewModel: ReservaViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReservaViewModel() as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {

        // ---------- LOGIN ----------
        composable(Route.Login.path) {
            LoginScreen(
                authViewModel = authViewModel,
                onGoRegister = {
                    navController.navigate(Route.Register.path)
                },
                onLoginClick = { email, pass ->
                    authViewModel.login(email, pass) { user ->
                        val destino =
                            if (user.rolId == 1) Route.Admin.path
                            else Route.Home.path

                        navController.navigate(destino) {
                            popUpTo(Route.Login.path) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ---------- REGISTRO ----------
        composable(Route.Register.path) {
            RegisterScreen(
                usuariosViewModel = usuariosViewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ---------- DASHBOARD CLIENTE ----------
        composable(Route.Home.path) {
            DashboardCliente(
                onGoAddReserva = {
                    navController.navigate("AddReserva/2")
                },
                onGoPerfil = {
                    navController.navigate(Route.Perfil.path)
                },
                onGoConsultaReserva = {
                    navController.navigate(Route.Consultar.path)
                }
            )
        }

        // ---------- DASHBOARD ADMIN ----------
        composable(Route.Admin.path) {
            DashboardAdmin(
                reservaViewModel = reservaViewModel,
                onGoAddReserva = {
                    navController.navigate("AddReserva/1")
                },
                onGoHome = {
                    navController.navigate(Route.Admin.path) {
                        popUpTo(Route.Admin.path) { inclusive = true }
                    }
                },
                onGoPerfil = {
                    navController.navigate(Route.Perfil.path)
                },
                onGoConsultaReserva = {
                    navController.navigate(Route.Consultar.path)
                },
                onGoLogs = {
                    navController.navigate(Route.Logs.path)
                }
            )
        }

        // ---------- ADD RESERVA ----------
        composable(
            route = "AddReserva/{piscinaId}",
            arguments = listOf(
                navArgument("piscinaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val piscinaId = backStackEntry.arguments?.getInt("piscinaId") ?: 1
            val esAdmin = authViewModel.userLogueado?.rolId == 1

            AddReserva(
                navController = navController,
                reservaViewModel = reservaViewModel,
                piscinaId = piscinaId,
                esAdmin = esAdmin,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- CONFIRMAR RESERVA ----------
        composable(
            route = "ConfirmarReserva/{fecha}/{piscinaId}/{esAdmin}",
            arguments = listOf(
                navArgument("fecha") { type = NavType.StringType },
                navArgument("piscinaId") { type = NavType.IntType },
                navArgument("esAdmin") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val piscinaId = backStackEntry.arguments?.getInt("piscinaId") ?: 1
            val esAdmin = backStackEntry.arguments?.getBoolean("esAdmin") ?: false

            ConfirmarReserva(
                fechaSeleccionada = fecha,
                piscinaId = piscinaId,
                esAdmin = esAdmin,
                reservaViewModel = reservaViewModel,
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onConfirmar = {
                    val destino =
                        if (esAdmin) Route.Admin.path
                        else Route.Home.path

                    navController.navigate(destino) {
                        popUpTo(destino) { inclusive = true }
                    }
                }
            )
        }

        // ---------- CONSULTA ----------
        composable(Route.Consultar.path) {
            ConsultaReserva(
                reservaViewModel = reservaViewModel,
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- PERFIL ----------
        composable(Route.Perfil.path) {
            VerPerfil(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Logs.path) {
            LogsScreen(
                onGoHome = {
                    navController.navigate(Route.Admin.path) {
                        popUpTo(Route.Admin.path) { inclusive = true }
                    }
                },
                onGoPerfil = { navController.navigate(Route.Perfil.path) },
                onGoConsultaReserva = { navController.navigate(Route.Consultar.path) },
                onGoLogs = { /* ya estás aquí */ }
            )
        }
    }
}

