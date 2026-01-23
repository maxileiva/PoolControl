package com.example.poolcontrol.data.local.piscina

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity

@Dao
interface PiscinaDao {

    // Se usa REPLACE para que si insertas una piscina con un ID que ya existe,
    // se actualicen los datos (como el horario o la imagen)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(piscina: PiscinaEntity)

    //llenar slider
    @Query("SELECT * FROM piscinas")
    suspend fun obtenerTodas(): List<PiscinaEntity>

    // Para obtener los detalles de la piscina que el usuario seleccionó en el slider
    @Query("SELECT * FROM piscinas WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): PiscinaEntity?

    @Update
    suspend fun actualizar(piscina: PiscinaEntity)

    @Delete
    suspend fun eliminar(piscina: PiscinaEntity)
}