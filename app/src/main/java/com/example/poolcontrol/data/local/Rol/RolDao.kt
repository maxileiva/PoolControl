package com.example.poolcontrol.data.local.Rol

import androidx.room.Query


interface RolDao {
    @Query("SELECT * FROM roles")
    suspend fun obtenerTodos(): List<RolEntity>

    // Para buscar un rol específico por su ID
    @Query("SELECT * FROM roles WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): RolEntity?
}