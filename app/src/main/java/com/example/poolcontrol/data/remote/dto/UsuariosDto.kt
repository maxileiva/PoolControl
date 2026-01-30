package com.example.poolcontrol.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserResponse(
    val id: Long,
    val nombre: String,
    val apellido: String?, // Agrégalo aquí
    val correo: String,
    val telefono: String?, // Agrégalo aquí
    val rol: RolData? = null
) {
    val rolId: Int get() = rol?.id?.toInt() ?: 3
}

data class RolData(
    val id: Long,
    val nombre: String
)
// DTO para el envío del Login
data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String,
    val telefono: String,
    // Tu Java hace: req.getRolNombre(), por eso la llave debe ser rolNombre
    val rolNombre: String = "CLIENTE"
)