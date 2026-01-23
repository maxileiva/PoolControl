package com.example.poolcontrol.data.local.Piscina

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "piscinas")
data class PiscinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val imagenRes: Int, //imagenes
    val horarioApertura: String,
    val horarioCierre: String
)