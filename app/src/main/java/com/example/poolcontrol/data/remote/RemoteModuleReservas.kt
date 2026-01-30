package com.example.poolcontrol.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteModuleReservas {
    private const val BASE_URL = "http://10.0.2.2:8091/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHTTP = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHTTP)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Esta es la línea clave que conecta con tu interfaz ReservasApi
    val api: ReservasApi = retrofit.create(ReservasApi::class.java)
}