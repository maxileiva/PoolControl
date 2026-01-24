// Archivo: app/src/main/java/com/example/poolcontrol/data/local/rol/RolDao.kt
package com.example.poolcontrol.data.local.rol // UNIFICADO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(rol: RolEntity): Long

    @Query("SELECT * FROM roles ORDER BY id ASC")
    suspend fun getAll(): List<RolEntity>

    // Agrega esta consulta para que el repositorio la use
    @Query("SELECT * FROM roles WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RolEntity?

    @Query("SELECT COUNT(*) FROM roles")
    suspend fun count(): Int
}