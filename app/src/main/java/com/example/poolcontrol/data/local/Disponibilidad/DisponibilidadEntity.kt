package com.example.poolcontrol.data.local.disponibilidad

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [Index(value = ["fecha", "piscinaId"], unique = true)]
)
data class DisponibilidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fecha: String,      // Formato "yyyy-MM-dd"
    val piscinaId: Int,
    val estaDisponible: Boolean = true
)