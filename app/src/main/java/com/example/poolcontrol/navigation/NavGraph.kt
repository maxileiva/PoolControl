package com.example.poolcontrol.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.poolcontrol.ui.screen.*
import com.example.poolcontrol.ui.viewmodel.AuthViewModel
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import com.example.poolcontrol.data.local.database.AppDatabase
import com.example.poolcontrol.data.repository.ReservaRepository

@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)

    // ViewModel de Auth (API Retrofit)
    val authViewModel: AuthViewModel = viewModel()

    // ViewModel de Reservas (Room Local)
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
        // --- PANTALLA DE LOGIN ---
        composable(Route.Login.path) {
            LoginScreen(
                authViewModel = authViewModel,
                onGoRegister = { navController.navigate(Route.Register.path) },
                onLoginClick = { email, pass ->
                    authViewModel.login(email, pass, onSuccess = { user ->
                        Log.d("NAV_DEBUG", "Login exitoso. Usuario: ${user.correo}, Rol ID: ${user.rolId}")

                        if (user.rolId == 1) {
                            navController.navigate(Route.Admin.path) {
                                popUpTo(Route.Login.path) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Route.Home.path) {
                                popUpTo(Route.Login.path) { inclusive = true }
                            }
                        }
                    })
                }
            )
        }

        // --- PANTALLA DE REGISTRO ---
        composable(Route.Register.path) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        } // <--- AQUÍ FALTABA ESTA LLAVE

        // --- DASHBOARD CLIENTE (ID 3) ---
        composable(Route.Home.path) {
            DashboardCliente(
                onGoAddReserva = { /* Implementar navegación */ },
                onGoPerfil = { navController.navigate(Route.Perfil.path) },
                onGoConsultaReserva = { /* Implementar navegación */ }
            )
        }

        // --- DASHBOARD ADMIN (ID 1) ---
        composable(Route.Admin.path) {
            DashboardAdmin(
                reservaViewModel = reservaViewModel,
                onGoAddReserva = { /* Implementar navegación */ },
                onGoPerfil = { navController.navigate(Route.Perfil.path) },
                onGoConsultaReserva = { /* Implementar navegación */ },
                onGoDashboardAdmin = { /* Ya estamos aquí */ }
            )
        }

        // --- VER PERFIL ---
        composable(Route.Perfil.path) {
            VerPerfil(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    // Limpia todo el historial para que no puedan volver atrás al perfil
                    navController.navigate(Route.Login.path) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}