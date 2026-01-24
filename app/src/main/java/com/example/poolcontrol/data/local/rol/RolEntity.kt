package com.example.poolcontrol.data.local.rol

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "roles")
data class RolEntity(
    @PrimaryKey
    val id: Int,
    val nombreRol: String
)
