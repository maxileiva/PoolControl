package com.example.poolcontrol.data.local.repository

import com.example.poolcontrol.data.local.user.UserDao
import com.example.poolcontrol.data.local.user.UserEntity
import android.util.Log

class UserRepository(private val userDao: UserDao) {

    suspend fun register(
        nombre: String, apellido: String, email: String, password: String,
        numero: String, rolId: Int
    ): Result<Long> {
        return try {
            val exists = userDao.getByEmail(email) != null
            if (exists) {
                Result.failure(Exception("El correo ya está registrado"))
            } else {
                val nuevoUsuario = UserEntity(
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    password = password,
                    numero = numero,
                    rolId = rolId
                )
                val id = userDao.insertar(nuevoUsuario)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, pass: String): Result<UserEntity> {
        Log.d("LOGIN_DEBUG", "Intentando login con: Email: '$email'")
        val user = userDao.getByEmail(email)

        return if (user == null) {
            Result.failure(Exception("CREDENCIALES INVALIDAS"))
        } else if (user.password == pass) {
            Result.success(user)
        } else {
            Result.failure(Exception("CREDENCIALES INVALIDAS"))
        }
    }

    suspend fun actualizarDatosUsuario(usuario: UserEntity): Int {
        return userDao.actualizarUsuario(usuario)
    }
}