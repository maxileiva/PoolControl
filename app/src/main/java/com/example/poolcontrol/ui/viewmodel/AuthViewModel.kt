package com.example.poolcontrol.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.local.repository.UserRepository
import com.example.poolcontrol.data.local.reserva.ReservaEntity
import com.example.poolcontrol.data.local.rol.RolEntity
import com.example.poolcontrol.data.local.user.UserEntity
import com.example.poolcontrol.data.repository.RolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RolUiState(
    val roles: List<RolEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val selectedRol: RolEntity? = null
)

class AuthViewModel(
    private val userRepository: UserRepository,
    private val rolRepository: RolRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var numero by mutableStateOf("")

    var userLogueado by mutableStateOf<UserEntity?>(null)
    var mensajeError by mutableStateOf<String?>(null)

    // ESTO CORRIGE TUS ERRORES EN ROJO
    val errorEmail: String?
        get() = if (email.isNotEmpty() && !email.contains("@")) "Email inválido" else null

    val errorPassword: String?
        get() = if (password.isNotEmpty() && password.length < 3) "Mínimo 3 caracteres" else null

    val loginValido: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && errorEmail == null && errorPassword == null

    // Validaciones adicionales para Registro
    val errorNombre: String?
        get() = if (nombre.isNotEmpty() && nombre.length < 2) "Nombre muy corto" else null

    val errorApellido: String?
        get() = if (apellido.isNotEmpty() && apellido.length < 2) "Apellido muy corto" else null

    val errorNumero: String?
        get() = if (numero.isNotEmpty() && numero.length < 8) "Número inválido" else null

    val formularioValido: Boolean
        get() = loginValido && nombre.isNotEmpty() && apellido.isNotEmpty() &&
                numero.isNotEmpty() && errorNombre == null &&
                errorApellido == null && errorNumero == null

    private val _uiState = MutableStateFlow(RolUiState())
    val uiState: StateFlow<RolUiState> = _uiState

    init {
        obtenerTodosLosRoles()
    }

    fun obtenerTodosLosRoles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMsg = null) }
            try {
                // Se usa la instancia 'rolRepository', no la clase
                val lista = rolRepository.obtenerTodosLosRoles()
                _uiState.update { it.copy(roles = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMsg = e.message ?: "Error al cargar roles")
                }
            }
        }
    }

    fun seleccionarRol(id: Int) {
        viewModelScope.launch {
            try {
                // Se usa la instancia 'rolRepository'
                val rol = rolRepository.obtenerRolPorId(id)
                _uiState.update { it.copy(selectedRol = rol) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMsg = "No se encontró el rol") }
            }
        }
    }

    fun login(onSuccess: (UserEntity) -> Unit) {
        viewModelScope.launch {
            mensajeError = null
            val resultado = userRepository.login(email, password)
            if (resultado.isSuccess) {
                val user = resultado.getOrNull()
                userLogueado = user
                if (user != null) onSuccess(user)
            } else {
                mensajeError = resultado.exceptionOrNull()?.message ?: "Error al iniciar sesión"
            }
        }
    }

    fun registrar(onSuccess: () -> Unit) {
        viewModelScope.launch {
            mensajeError = null
            val resultado = userRepository.register(
                nombre, apellido, email, password, numero, 3 // 3 = Rol Cliente
            )
            if (resultado.isSuccess) {
                onSuccess()
            } else {
                mensajeError = resultado.exceptionOrNull()?.message ?: "Error al registrar"
            }
        }
    }

    // Agrega esto dentro de tu clase AuthViewModel
    fun actualizarPerfil(
        nuevoNombre: String,
        nuevoApellido: String,
        nuevoNumero: String,
        onResult: (Boolean) -> Unit // Cambiamos onSuccess por onResult con booleano
    ) {
        viewModelScope.launch {
            val usuarioActual = userLogueado
            if (usuarioActual != null) {
                // Creamos la copia manteniendo el ID original para que Room lo encuentre
                val usuarioEditado = usuarioActual.copy(
                    nombre = nuevoNombre,
                    apellido = nuevoApellido,
                    numero = nuevoNumero
                )

                val filasAfectadas = userRepository.actualizarDatosUsuario(usuarioEditado)

                if (filasAfectadas > 0) {
                    userLogueado = usuarioEditado // Actualizamos la sesión en memoria
                    onResult(true)
                } else {
                    onResult(false)
                }
            } else {
                onResult(false)
            }
        }
    }
    }
