package com.example.poolcontrol.data.local.Piscina

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "piscinas")
data class PiscinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val imagenRes: Int, // ID del recurso drawable
    val horarioApertura: String, // Ejemplo: "08:00"
    val horarioCierre: String    // Ejemplo: "20:00"
)