package com.example.poolcontrol.data.local.dao

import androidx.room.*
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity
import com.example.poolcontrol.data.local.reserva.ReservaEntity
import com.example.poolcontrol.data.local.disponibilidad.DisponibilidadEntity

@Dao
interface ReservaPiscinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPiscina(piscina: PiscinaEntity)

    @Query("SELECT * FROM piscinas")
    suspend fun obtenerTodasLasPiscinas(): List<PiscinaEntity>

    // Esta es la consulta clave para el calendario
    @Query("SELECT fecha FROM reservas WHERE piscinaId = :piscinaId")
    suspend fun obtenerFechasReservadas(piscinaId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun actualizarDisponibilidad(disponibilidad: DisponibilidadEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarReserva(reserva: ReservaEntity): Long

    @Query("SELECT * FROM reservas ORDER BY fecha ASC")
    suspend fun obtenerTodasLasReservas(): List<ReservaEntity>

    @Delete
    suspend fun eliminarReserva(reserva: ReservaEntity)
}