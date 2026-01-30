package com.example.poolcontrol.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.remote.UsuariosApi
import com.example.poolcontrol.data.remote.RemoteModuleUsuarios
import com.example.poolcontrol.data.remote.dto.LoginRequest
import com.example.poolcontrol.data.remote.dto.RegisterRequest
import com.example.poolcontrol.data.remote.dto.UserResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var userLogueado by mutableStateOf<UserResponse?>(null)
    var mensajeError by mutableStateOf<String?>(null)

    private val authApi: UsuariosApi = RemoteModuleUsuarios.create(UsuariosApi::class.java)

    // PROPIEDAD RESTAURADA: Valida que el email tenga @ y la clave al menos 3 caracteres
    val loginValido: Boolean get() = email.contains("@") && password.length >= 3

    fun limpiarCampos() {
        email = ""
        password = ""
        mensajeError = null
    }

    fun login(correo: String, pass: String, onSuccess: (UserResponse) -> Unit) {
        viewModelScope.launch {
            mensajeError = null
            try {
                val response = authApi.login(LoginRequest(correo, pass))
                if (response.isSuccessful && response.body() != null) {
                    userLogueado = response.body()
                    onSuccess(userLogueado!!)
                } else {
                    mensajeError = "Correo o contraseña incorrectos"
                }
            } catch (e: Exception) {
                mensajeError = "Servidor no disponible"
            }
        }
    }

    fun registrar(nom: String, ape: String, cor: String, pas: String, tel: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(nom, ape, cor, pas, tel, "CLIENTE")
                val response = authApi.register(request)
                if (response.isSuccessful) {
                    onResult(true, "¡Registro exitoso!")
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val mensaje = when {
                        errorBody.contains("correo", true) -> "Este correo ya está registrado"
                        errorBody.contains("telefono", true) -> "Este teléfono ya está registrado"
                        else -> "Error en el servidor"
                    }
                    onResult(false, mensaje)
                }
            } catch (e: Exception) {
                onResult(false, "Error de conexión")
            }
        }
    }

    fun actualizarDatosRemotos(nom: String, ape: String, tel: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val id = userLogueado?.id ?: return@launch
                val response = authApi.actualizarPerfil(id, mapOf("nombre" to nom, "apellido" to ape, "telefono" to tel))
                if (response.isSuccessful && response.body() != null) {
                    userLogueado = response.body()
                    onResult(true)
                } else onResult(false)
            } catch (e: Exception) { onResult(false) }
        }
    }

    fun cambiarContrasenaRemoto(actual: String, nueva: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val id = userLogueado?.id ?: return@launch
                val response = authApi.cambiarContrasena(id, mapOf("contrasenaActual" to actual, "nuevaContrasena" to nueva))
                if (response.isSuccessful) onResult(true, "Contraseña actualizada")
                else onResult(false, "Clave actual incorrecta")
            } catch (e: Exception) { onResult(false, "Error de red") }
        }
    }
}