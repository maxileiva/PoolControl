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

    fun login(correo: String, password: String) {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            // Usamos 'contrasena = password' para mapear el nombre del Repo con el del VM
            repository.login(correo = correo, contrasena = password)
                .onSuccess { user ->
                    uiState = uiState.copy(isLoading = false, usuario = user, success = true, error = null)
                }
                .onFailure { e ->
                    uiState = uiState.copy(isLoading = false, error = e.message, success = false)
                }
        }
    }

    fun registrar(nombre: String, apellido: String, correo: String, telefono: String, password: String) {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repository.registrarUsuario(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                telefono = telefono,
                contrasena = password, // Arreglado: era 'password', no 'contrasenaz'
                rolNombre = "CLIENTE"  // Agregado: el Repo lo necesita y lo asignamos auto
            )
                .onSuccess { user ->
                    uiState = uiState.copy(isLoading = false, usuario = user, success = true, error = null)
                }
                .onFailure { e ->
                    uiState = uiState.copy(isLoading = false, error = e.message, success = false)
                }
        }
    }
}