package com.example.poolcontrol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LogDTO(
    val nivel: String,
    val mensaje: String,
    val servicio: String,
    val fecha: String?
)
