package com.example.poolcontrol.data.local.user

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//indicamos las operaciones (sentencias sql) que admite la tabla
//dao y entity para cada tabla


interface UserDao {

//permitir que acepte insert
    @Insert(onConflict = OnConflictStrategy.ABORT)   //-> abort realiza rolback
    suspend fun insertar(User: UserEntity): Long
     //devuelvo id por si necesito asociarlo a una reserva para que me devuelva automaticamente el id

    //para hacer select //dar que sentencia SQL //ejemplo id existe   //ver si el user esta registrado
    //mostrar datos en el perfil
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    //buscar todos los usuarios //editar esto para buscar todas las reservas
    //@Query("SELECT * FROM USERS")
    //suspend fun  getAll(): List<UserEntity>

    //contar cuantos usuarios existen
    //@Query("SELECT COUNT(*) FROM users")
    //suspend fun count(): Int



}