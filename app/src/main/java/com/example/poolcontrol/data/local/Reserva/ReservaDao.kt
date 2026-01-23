package com.example.poolcontrol.data.local.Reserva

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


interface ReservaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(reserva: ReservaEntity): Long

    // Requisito del profesor: Ver sus propias reservas
    @Query("SELECT * FROM reservas WHERE userId = :idUsuario")
    suspend fun getReservasPorUsuario(idUsuario: Long): List<ReservaEntity>

    // Requisito del profesor: Modificar reserva
    @Update
    suspend fun modificar(reserva: ReservaEntity)

    @Delete
    suspend fun eliminar(reserva: ReservaEntity)
}