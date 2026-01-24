package com.example.poolcontrol.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.local.reserva.ReservaEntity
import com.example.poolcontrol.data.repository.ReservaRepository
import kotlinx.coroutines.launch

class ReservaViewModel(private val repository: ReservaRepository) : ViewModel() {

    // Lista observable para que el calendario sepa qué días bloquear
    var fechasBloqueadas by mutableStateOf<List<String>>(emptyList())
        private set

    var todasLasReservas by mutableStateOf<List<ReservaEntity>>(emptyList())
        private set

    fun cargarTodasLasReservas() {
        viewModelScope.launch {
            try {
                // Ahora 'repository' ya reconoce esta función
                todasLasReservas = repository.obtenerTodasLasReservas()
            } catch (e: Exception) {
                todasLasReservas = emptyList()
            }
        }
    }
    fun cargarFechasOcupadas(piscinaId: Int) {
        viewModelScope.launch {
            try {
                fechasBloqueadas = repository.obtenerFechasOcupadas(piscinaId)
            } catch (e: Exception) {
                fechasBloqueadas = emptyList()
            }
        }
    }

    /**
     * Guarda la reserva en la base de datos y actualiza la lista de bloqueos.
     */
    fun realizarReserva(
        fecha: String,
        nombre: String,
        telefono: String,
        cantidad: Int,
        piscinaId: Int,
        userId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val nuevaReserva = ReservaEntity(
                fecha = fecha,
                nombreCliente = nombre,
                telefonoCliente = telefono,
                cant_personas = cantidad,
                userId = userId,
                piscinaId = piscinaId
            )

            val resultado = repository.guardarReserva(nuevaReserva)

            if (resultado.isSuccess) {
                // Refrescamos las fechas bloqueadas inmediatamente después de guardar
                cargarFechasOcupadas(piscinaId)
                onSuccess()
            } else {
                onError(resultado.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun eliminarReserva(reserva: ReservaEntity) {
        viewModelScope.launch {
            repository.eliminarReserva(reserva) // Crea esta función en el repo que llame a dao.delete(reserva)
            cargarTodasLasReservas() // Recarga la lista automáticamente
        }
    }
}