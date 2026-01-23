package com.example.poolcontrol.data.local.Reserva

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.poolcontrol.data.local.user.UserEntity
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity

@Entity(
    tableName = "reservas",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = PiscinaEntity::class, parentColumns = ["id"], childColumns = ["piscinaId"])
    ]
)
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true)
    val idReserva: Long = 0L,
    val fecha: String,
    val nombreCliente: String,   // El "nombreCompleto" de tu formulario
    val telefonoCliente: String, // El "telefono" de tu formulario
    val cant_personas: Int,      // La "cantidadPersonas" de tu formulario
    val userId: Long,            // El ID del usuario que inició sesión
    val piscinaId: Int           // El ID de la piscina elegida en el slider
)