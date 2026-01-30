package com.example.poolcontrol.data.local.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    @PrimaryKey(autoGenerate = false) // El ID lo dicta la base de datos de Laragon
    val id: Long,
    val nombre: String,
    val apellido: String,
    val correo: String,      // Sincronizado con el backend
    val contrasena: String? = null, // Puede ser null porque el backend no la devuelve por seguridad
    val telefono: String,    // Sincronizado con el backend
    val rolId: Int
)