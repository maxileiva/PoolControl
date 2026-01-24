package com.example.poolcontrol.data.repository

import com.example.poolcontrol.data.local.rol.RolDao
import com.example.poolcontrol.data.local.rol.RolEntity

class RolRepository(private val rolDao: RolDao) {

    suspend fun obtenerTodosLosRoles(): List<RolEntity> = rolDao.getAll()

    suspend fun contarRoles(): Int = rolDao.count()

    suspend fun insertarRol(rol: RolEntity) = rolDao.insertar(rol)

    // AGREGA ESTA FUNCIÓN QUE FALTABA:
    // Cambia la función en RolRepository por esta:
    suspend fun obtenerRolPorId(id: Int): RolEntity? = rolDao.getById(id)

}