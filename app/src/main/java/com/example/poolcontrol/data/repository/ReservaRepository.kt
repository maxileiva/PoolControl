package com.example.poolcontrol.data.repository

import com.example.poolcontrol.data.local.dao.ReservaPiscinaDao
import com.example.poolcontrol.data.local.reserva.ReservaEntity
import com.example.poolcontrol.data.local.disponibilidad.DisponibilidadEntity
import java.text.SimpleDateFormat
import java.util.*

class ReservaRepository(private val dao: ReservaPiscinaDao) {

    private fun obtenerHoy(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun obtenerFechasOcupadas(piscinaId: Int): List<String> {
        // Ahora coincide con el nombre en el DAO
        return dao.obtenerFechasReservadas(piscinaId)
    }

    suspend fun guardarReserva(reserva: ReservaEntity): Result<Long> {
        val hoy = obtenerHoy()
        if (reserva.fecha < hoy) return Result.failure(Exception("No puedes reservar fechas pasadas"))

        return try {
            val id = dao.insertarReserva(reserva)
            // Marcamos disponibilidad como ocupada (estaDisponible = false)
            dao.actualizarDisponibilidad(
                DisponibilidadEntity(fecha = reserva.fecha, piscinaId = reserva.piscinaId, estaDisponible = false)
            )
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerTodasLasReservas(): List<ReservaEntity> {
        return dao.obtenerTodasLasReservas()
    }
    suspend fun eliminarReserva(reserva: ReservaEntity) {
        dao.eliminarReserva(reserva)
    }
}