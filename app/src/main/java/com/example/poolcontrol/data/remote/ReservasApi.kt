package com.example.poolcontrol.data.remote

import com.example.poolcontrol.data.network.ReservaDto
import retrofit2.Response
import retrofit2.http.*

interface ReservasApi {
    @GET("api/reservas")
    suspend fun obtenerTodas(): Response<List<ReservaDto>>

    @GET("api/reservas/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): Response<ReservaDto>

    @POST("api/reservas")
    suspend fun crearReserva(@Body reserva: ReservaDto): Response<ReservaDto>

    @DELETE("api/reservas/{id}")
    suspend fun eliminarReserva(@Path("id") id: Long): Response<Unit>

    @PUT("api/reservas/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Long,
        @Query("nuevoEstado") estado: String
    ): Response<ReservaDto>
}