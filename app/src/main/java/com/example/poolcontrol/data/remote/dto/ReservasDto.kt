package com.example.poolcontrol.data.network

data class ReservaDto(
    val id: Long? = null, // <--- CAMBIADO A NULABLE
    val fecha: String,
    val nombrePiscina: String,
    val detalles: String,
    val precio: Double,
    val idCliente: Long,
    val estado: String
)