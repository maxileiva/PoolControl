package com.example.poolcontrol.navigation

//clase sellada para evitar rutas mal escritas o duplicadas

sealed class Route(val path: String) {
    object Login : Route("login")
    object Home : Route("home")
    object Admin : Route("admin")
    object Register : Route("register")
    object AddReserva : Route("addReserva")
    object Perfil : Route("perfil")
    object Consultar : Route("ConsultaReserva")
    object Confirmar : Route("ConfirmarReserva/{fecha}/{piscinaId}/{esAdmin}")
    object Logs : Route("Logs") // Nueva ruta para logs
}
