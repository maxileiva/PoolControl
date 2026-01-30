package com.example.poolcontrol.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.remote.RemoteModuleReservas
import com.example.poolcontrol.data.network.ReservaDto
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {
    var todasLasReservas = mutableStateListOf<ReservaDto>()
    var reservasCliente = mutableStateListOf<ReservaDto>()
    var fechasBloqueadas = mutableStateListOf<String>()
        private set

    private val api = RemoteModuleReservas.api

    // FUNCIÓN PARA CREAR RESERVA (La que estaba dando error)
    fun realizarReserva(
        fecha: String,
        nombrePiscina: String,
        detalles: String,
        precio: Double,
        idCliente: Long,
        estado: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val dto = ReservaDto(
                    id = null, // Importante para que el backend genere uno nuevo
                    fecha = fecha,
                    nombrePiscina = nombrePiscina,
                    detalles = detalles,
                    precio = precio,
                    idCliente = idCliente,
                    estado = estado
                )
                val response = api.crearReserva(dto)
                if (response.isSuccessful) {
                    cargarFechasOcupadas() // Refrescar calendario
                    onSuccess()
                } else {
                    onError("Servidor respondió con error ${response.code()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión")
            }
        }
    }

    fun cargarFechasOcupadas() {
        viewModelScope.launch {
            try {
                val response = api.obtenerTodas()
                if (response.isSuccessful) {
                    fechasBloqueadas.clear()
                    val fechas = response.body()?.filter { it.estado != "RECHAZO APROBADO" }?.map { it.fecha } ?: emptyList()
                    fechasBloqueadas.addAll(fechas)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun cargarTodasLasReservas() {
        viewModelScope.launch {
            try {
                val response = api.obtenerTodas()
                if (response.isSuccessful) {
                    todasLasReservas.clear()
                    response.body()?.let { todasLasReservas.addAll(it) }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun cargarReservasPorCliente(idCliente: Long) {
        viewModelScope.launch {
            try {
                val response = api.obtenerTodas()
                if (response.isSuccessful) {
                    reservasCliente.clear()
                    val filtradas = response.body()?.filter { it.idCliente == idCliente } ?: emptyList()
                    reservasCliente.addAll(filtradas)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun actualizarEstadoReserva(id: Long, nuevoEstado: String, esAdmin: Boolean, clienteId: Long = 0) {
        viewModelScope.launch {
            try {
                val response = api.actualizarEstado(id, nuevoEstado)
                if (response.isSuccessful) {
                    if (esAdmin) cargarTodasLasReservas() else cargarReservasPorCliente(clienteId)
                    cargarFechasOcupadas()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun decidirCancelacion(id: Long, aceptada: Boolean, nota: String, esAdmin: Boolean, reserva: ReservaDto, clienteId: Long) {
        viewModelScope.launch {
            try {
                val nuevoEstado = if (aceptada) "RECHAZO APROBADO" else "CANCELACION RECHAZADA"
                val detalleLimpio = reserva.detalles.substringBefore(" | NOTA ADMIN:")
                val nuevosDetalles = "$detalleLimpio | NOTA ADMIN: ${nota.ifEmpty { "Sin observaciones" }}"
                val reservaUpdate = reserva.copy(estado = nuevoEstado, detalles = nuevosDetalles)
                val response = api.crearReserva(reservaUpdate)
                if (response.isSuccessful) {
                    if (esAdmin) cargarTodasLasReservas() else cargarReservasPorCliente(clienteId)
                    cargarFechasOcupadas()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun eliminarReserva(id: Long) {
        viewModelScope.launch {
            try {
                val response = api.eliminarReserva(id)
                if (response.isSuccessful) {
                    todasLasReservas.removeIf { it.id == id }
                    reservasCliente.removeIf { it.id == id }
                    cargarFechasOcupadas()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}