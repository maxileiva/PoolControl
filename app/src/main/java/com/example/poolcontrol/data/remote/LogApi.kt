package com.example.poolcontrol.data.remote

import com.example.poolcontrol.data.remote.dto.LogDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LogApi {

    @GET("/logs")
    suspend fun obtenerLogs(): List<LogDTO>

    @POST("/logs")
    suspend fun crearLog(@Body log: LogDTO)
}
