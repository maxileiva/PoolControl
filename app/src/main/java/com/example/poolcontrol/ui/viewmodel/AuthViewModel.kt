package com.example.poolcontrol.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.remote.*
import com.example.poolcontrol.data.remote.dto.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // --- ESTADOS PARA LOGIN ---
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)

    val loginValido: Boolean get() = email.contains("@") && password.length >= 4

    fun limpiarCampos() {
        email = ""
        password = ""
        mensajeError = null
    }

    // --- ESTADO DE SESIÓN ---
    var userLogueado by mutableStateOf<UserResponse?>(null)

    private val authApi: UsuariosApi = RemoteModuleUsuarios.create(UsuariosApi::class.java)

    // LOGIN
    fun login(correo: String, pass: String, onSuccess: (UserResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = authApi.login(LoginRequest(correo, pass))
                if (response.isSuccessful) {
                    userLogueado = response.body()
                    onSuccess(userLogueado!!)
                } else {
                    mensajeError = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión"
            }
        }
    }

    // ACTUALIZAR PERFIL (Datos generales y Foto)
    fun actualizarPerfilCompleto(nom: String, ape: String, tel: String, foto: String?, onResult: (Boolean, String) -> Unit) {
        val user = userLogueado ?: return
        val datos = mapOf(
            "nombre" to nom,
            "apellido" to ape,
            "telefono" to tel,
            "fotoPerfil" to (foto ?: user.fotoPerfil ?: "")
        )
        viewModelScope.launch {
            try {
                val response = authApi.actualizarPerfil(user.id, datos)
                if (response.isSuccessful) {
                    userLogueado = response.body()
                    onResult(true, "Perfil actualizado")
                } else {
                    onResult(false, if (response.code() == 409) "El teléfono ya existe" else "Error al actualizar")
                }
            } catch (e: Exception) {
                onResult(false, "Error de red")
            }
        }
    }

    // CAMBIAR CONTRASEÑA (Llama a @PUT("api/usuarios/{id}/contrasena"))
    fun cambiarContrasenaRemoto(actual: String, nueva: String, onResult: (Boolean, String) -> Unit) {
        val user = userLogueado ?: return
        val datos = mapOf("contrasenaActual" to actual, "nuevaContrasena" to nueva)
        viewModelScope.launch {
            try {
                val response = authApi.cambiarContrasena(user.id, datos)
                if (response.isSuccessful) {
                    onResult(true, "Contraseña cambiada con éxito")
                } else {
                    onResult(false, "La contraseña actual es incorrecta")
                }
            } catch (e: Exception) {
                onResult(false, "Error de comunicación")
            }
        }
    }

    // RECUPERAR CONTRASEÑA
    fun recuperarPassword(cor: String, tel: String, nueva: String, onResult: (Boolean, String) -> Unit) {
        val datos = mapOf("correo" to cor, "telefono" to tel, "nuevaContrasena" to nueva)
        viewModelScope.launch {
            try {
                val resp = authApi.recuperarPassword(datos)
                if (resp.isSuccessful) onResult(true, "Éxito") else onResult(false, "Datos incorrectos")
            } catch (e: Exception) {
                onResult(false, "Error de red")
            }
        }
    }
}