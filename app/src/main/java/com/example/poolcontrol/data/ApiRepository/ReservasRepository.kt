package com.example.poolcontrol.data.ApiRepository

import com.example.poolcontrol.data.remote.RemoteModuleReservas
import com.example.poolcontrol.data.network.ReservaDto

class ReservasRepository {
    // Cambiado para usar tu objeto RemoteModuleReservas
    private val api = RemoteModuleReservas.api

    suspend fun obtenerTodas() = api.obtenerTodas()

    suspend fun crear(reserva: ReservaDto) = api.crearReserva(reserva)

    suspend fun eliminar(id: Long) = api.eliminarReserva(id)

    suspend fun actualizarEstado(id: Long, nuevoEstado: String) =
        api.actualizarEstado(id, nuevoEstado)

    suspend fun obtenerFechasOcupadas(): List<String> {
        val respuesta = api.obtenerTodas()
        return if (respuesta.isSuccessful) {
            respuesta.body()?.map { it.fecha } ?: emptyList()
        } else {
            emptyList()
        }
    }
}