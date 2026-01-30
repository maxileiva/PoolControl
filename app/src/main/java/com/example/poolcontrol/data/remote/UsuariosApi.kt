package com.example.poolcontrol.data.remote

import com.example.poolcontrol.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @PUT("api/usuarios/{id}")
    suspend fun actualizarPerfil(@Path("id") id: Long, @Body datos: Map<String, String>): Response<UserResponse>

    @PUT("api/usuarios/{id}/password")
    suspend fun cambiarContrasena(@Path("id") id: Long, @Body passwords: Map<String, String>): Response<Unit>
}