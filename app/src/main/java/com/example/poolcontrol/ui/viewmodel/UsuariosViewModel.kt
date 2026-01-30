package com.example.poolcontrol.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.ApiRepository.UsuariosRepository
import com.example.poolcontrol.data.remote.dto.UserResponse
import kotlinx.coroutines.launch

data class UsuariosUiState(
    val isLoading: Boolean = false,
    val usuario: UserResponse? = null,
    val error: String? = null,
    val success: Boolean = false
)

class UsuariosViewModel(
    private val repository: UsuariosRepository = UsuariosRepository()
) : ViewModel() {

    var uiState by mutableStateOf(UsuariosUiState())
        private set

    // Resetear el estado manualmente desde el exterior
    fun limpiarEstado() {
        uiState = UsuariosUiState()
    }

    fun login(correo: String, password: String) {
        // Al iniciar, forzamos success = false y error = null
        uiState = uiState.copy(isLoading = true, error = null, success = false)
        viewModelScope.launch {
            repository.login(correo = correo, contrasena = password)
                .onSuccess { user ->
                    uiState = uiState.copy(isLoading = false, usuario = user, success = true)
                }
                .onFailure { e ->
                    uiState = uiState.copy(isLoading = false, error = e.message, success = false)
                }
        }
    }

    fun registrar(nombre: String, apellido: String, correo: String, telefono: String, password: String) {
        // EL AJUSTE CLAVE: Limpiar el estado anterior (especialmente 'success') antes de la corrutina
        uiState = uiState.copy(isLoading = true, error = null, success = false)

        viewModelScope.launch {
            repository.registrarUsuario(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                telefono = telefono,
                contrasena = password,
                rolNombre = "CLIENTE"
            )
                .onSuccess { user ->
                    // Éxito: actualizamos estado
                    uiState = uiState.copy(isLoading = false, usuario = user, success = true)
                }
                .onFailure { e ->
                    // Error: capturamos el mensaje para el Toast
                    uiState = uiState.copy(isLoading = false, error = e.message ?: "Error desconocido", success = false)
                }
        }
    }

    fun actualizarPerfil(id: Long, nombre: String, apellido: String, telefono: String) {
        uiState = uiState.copy(isLoading = true, success = false, error = null)
        viewModelScope.launch {
            val datos = mapOf("nombre" to nombre, "apellido" to apellido, "telefono" to telefono)
            try {
                val response = repository.actualizarPerfil(id, datos)
                if (response.isSuccessful) {
                    uiState = uiState.copy(isLoading = false, usuario = response.body(), success = true)
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Error al actualizar", success = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = "Error de red", success = false)
            }
        }
    }
}