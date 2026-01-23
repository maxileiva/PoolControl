package com.example.poolcontrol.data.local.Disponibilidad

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DisponibilidadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(disponibilidad: DisponibilidadEntity)

    // Consulta clave: Verifica si hay cupos en una fecha para una piscina específica
    @Query("SELECT * FROM disponibilidad WHERE piscinaId = :piscinaId AND fecha = :fecha LIMIT 1")
    suspend fun obtenerPorFechaYPiscina(piscinaId: Int, fecha: String): DisponibilidadEntity?

    @Update
    suspend fun actualizar(disponibilidad: DisponibilidadEntity)
}