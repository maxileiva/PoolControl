package com.example.poolcontrol.data.local.reserva

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity
import com.example.poolcontrol.data.local.user.UserEntity

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
    val nombreCliente: String,
    val telefonoCliente: String,
    val cant_personas: Int,
    val userId: Long,
    val piscinaId: Int
)