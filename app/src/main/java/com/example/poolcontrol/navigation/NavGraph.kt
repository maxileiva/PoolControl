package com.example.poolcontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.poolcontrol.data.local.repository.UserRepository
import com.example.poolcontrol.data.repository.RolRepository
import com.example.poolcontrol.data.repository.ReservaRepository
import com.example.poolcontrol.ui.screen.*
import com.example.poolcontrol.ui.viewmodel.AuthViewModel
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import com.example.poolcontrol.data.local.database.AppDatabase

@Composable
fun AppNavGraph(
    navController: NavHostController,
    repository: RolRepository
) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)

    // 1. AuthViewModel único para toda la navegación (mantiene sesión)
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(UserRepository(database.userDao()), repository) as T
            }
        }
    )

    // 2. ReservaViewModel único para el flujo de reserva (bloqueo y guardado)
    val reservaViewModel: ReservaViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReservaViewModel(ReservaRepository(database.reservaPiscinaDao())) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        val goLogin = { navController.navigate(Route.Login.path) { popUpTo(0) } }
        val goPerfil = { navController.navigate(Route.Perfil.path) }
        val goConsultaReserva = { navController.navigate(Route.Consultar.path) }
        val goDashAdmin = { navController.navigate(Route.Admin.path) }
        val goDashCli = { navController.navigate(Route.Home.path) }

        composable(Route.Login.path) {
            LoginScreen(
                authViewModel = authViewModel,
                onGoRegister = { navController.navigate(Route.Register.path) },
                onLoginClick = { _ ->
                    authViewModel.login(onSuccess = { user ->
                        if (user.rolId == 1) goDashAdmin() else goDashCli()
                    })
                }
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(
                authViewModel = authViewModel,
                onGoLogin = { navController.popBackStack() },
                onRegisterClick = { authViewModel.registrar(onSuccess = { navController.popBackStack() }) }
            )
        }

        composable(Route.Home.path) {
            DashboardCliente(
                onGoAddReserva = { navController.navigate("AddReserva/1/false") },
                onGoPerfil = goPerfil,
                onGoConsultaReserva = goConsultaReserva
            )
        }

        composable(Route.Admin.path) {
            DashboardAdmin(
                reservaViewModel = reservaViewModel, // <--- ESTO ES LO QUE FALTA
                onGoDashboardAdmin = goDashAdmin,
                onGoAddReserva = { navController.navigate("AddReserva/1/true") },
                onGoPerfil = goPerfil,
                onGoConsultaReserva = goConsultaReserva
            )
        }

        // --- PANTALLA CALENDARIO (CON BLOQUEO) ---
        composable(
            route = "AddReserva/{piscinaId}/{esAdmin}",
            arguments = listOf(
                navArgument("piscinaId") { type = NavType.IntType },
                navArgument("esAdmin") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val piscinaId = backStackEntry.arguments?.getInt("piscinaId") ?: 1
            val esAdmin = backStackEntry.arguments?.getBoolean("esAdmin") ?: false

            AddReserva(
                navController = navController,
                reservaViewModel = reservaViewModel, // Pasamos el VM para bloquear fechas
                onBack = { navController.popBackStack() },
                esAdmin = esAdmin,
                piscinaId = piscinaId
            )
        }

        // --- PANTALLA CONFIRMACIÓN (CON AUTO-RELLENO) ---
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
                    val destino = if (esAdmin) Route.Admin.path else Route.Home.path
                    navController.navigate(destino) {
                        popUpTo(destino) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Consultar.path) {
            ConsultaReserva(
                reservaViewModel = reservaViewModel,
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Perfil.path) {
            VerPerfil(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLogout = goLogin
            )
        }
    }
}