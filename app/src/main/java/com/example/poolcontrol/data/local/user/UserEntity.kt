package com.example.poolcontrol.data.local.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
// IMPORTANTE: Asegúrate de que esta ruta sea la correcta para tu RolEntity
import com.example.poolcontrol.data.local.rol.RolEntity

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = RolEntity::class,
            parentColumns = ["id"],
            childColumns = ["rolId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val numero: String,
    val rolId: Int
)