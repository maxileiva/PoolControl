package com.example.poolcontrol.data.ApiRepository

import com.example.poolcontrol.data.remote.RemoteModuleUsuarios
import com.example.poolcontrol.data.remote.UsuariosApi
import com.example.poolcontrol.data.remote.dto.LoginRequest
import com.example.poolcontrol.data.remote.dto.RegisterRequest
import com.example.poolcontrol.data.remote.dto.UserResponse
import retrofit2.Response

class UsuariosRepository(
    // Inyectamos la API. Si tu interfaz se llama JsonPlaceholder, úsala aquí.
    private val api: UsuariosApi = RemoteModuleUsuarios.create(UsuariosApi::class.java)
) {

    /**
     * Función para iniciar sesión.
     * Recibe correo y contraseña, y devuelve un Result con la respuesta del servidor.
     */
    suspend fun login(correo: String, contrasena: String): Result<UserResponse?> {
        return try {
            // Creamos el objeto con los datos del login
            val request = LoginRequest(correo, contrasena)

            // Llamamos al endpoint de la API
            val response = api.login(request)

            if (response.isSuccessful) {
                // Si el servidor responde 200 OK, devolvemos el usuario
                Result.success(response.body())
            } else {
                // Si las credenciales están mal (401), devolvemos un error
                Result.failure(Exception("Error en el servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Si no hay internet o el microservicio está apagado
            Result.failure(e)
        }
    }

    /**
     * Función para registrar un nuevo usuario.
     * Recibe los datos del formulario y devuelve el usuario creado.
     */
    suspend fun registrarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        telefono: String,
        contrasena: String,
        rolNombre: String // Ejemplo: "CLIENTE", "VETERINARIO" o "ADMINISTRATIVO"
    ): Result<UserResponse?> {
        return try {
            // 1. Preparamos el objeto con todos los campos que pide el Microservicio
            val nuevoUsuario = RegisterRequest(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                telefono = telefono,
                contrasena = contrasena,
            )

            // 2. Llamamos al endpoint de registro definido en JsonPlaceholder
            val response = api.register(nuevoUsuario)

            if (response.isSuccessful) {
                // Si el servidor responde 201 Created, devolvemos el usuario
                Result.success(response.body())
            } else {
                // Si el correo ya existe (409) o hay error de datos (400)
                Result.failure(Exception("Error en el registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión o servidor apagado
            Result.failure(e)
        }
    }

    /**
     * Función para actualizar el perfil (incluyendo la foto).
     */
    suspend fun actualizarPerfil(id: Long, datos: Map<String, String>): Response<UserResponse> {
        // Simplemente llamamos a la función que ya definiste en UsuariosApi
        return api.actualizarPerfil(id, datos)
    }

    suspend fun cambiarContrasena(id: Long, datos: Map<String, String>): Result<Unit> {
        return try {
            val response = api.cambiarContrasena(id, datos)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}