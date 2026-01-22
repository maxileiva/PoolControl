package com.example.poolcontrol.navigation

//clase sellada para evitar rutas mal escritas o duplicadas

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Home : Route("home")
    data object Admin : Route("admin")
    data object Register : Route("register")
    data object AddReserva : Route("addReserva")
    data object Perfil : Route("perfil")
    data object Consultar : Route("ConsultaReserva")
    data object Confirmar : Route("ConfirmarReserva/{fecha}")
}