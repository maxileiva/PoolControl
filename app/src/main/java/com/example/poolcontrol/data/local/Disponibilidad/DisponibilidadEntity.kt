package com.example.poolcontrol.data.local.Disponibilidad

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity

@Entity(
    tableName = "disponibilidad",
    foreignKeys = [
        ForeignKey(
            entity = PiscinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["piscinaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DisponibilidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val piscinaId: Int,
    val fecha: String,
    val cuposDisponibles: Int
)