package com.example.poolcontrol.ui.viewmodel // Asegúrate de que este sea tu package real

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
// IMPORTA TUS VALIDADCIONES (Ajusta el package según tu Validators.kt)
import com.example.poolcontrol.domain.validation.* class AuthViewModel : ViewModel() {

    // 1. ESTADOS DE TEXTO (Lo que el usuario escribe)
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var numero by mutableStateOf("")

    // 2. ESTADOS DE ERROR (Llaman a tus funciones de Validators.kt)
    val errorNombre get() = validateNameLettersOnly(nombre)
    val errorApellido get() = validateNameLettersOnly(apellido)
    val errorEmail get() = validateEmail(email)
    val errorPassword get() = validateStringPassword(password)
    val errorNumero get() = validatePhoneDigitsOnly(numero)

    // 3. VARIABLE QUE ACTIVA EL BOTÓN
    // Solo es true si TODOS los validadores devuelven null
    val formularioValido: Boolean
        get() = errorNombre == null &&
                errorApellido == null &&
                errorEmail == null &&
                errorPassword == null &&
                errorNumero == null &&
                nombre.isNotEmpty() &&
                apellido.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                numero.isNotEmpty()

    // Variable que activa el botón de Login
    val loginValido: Boolean
        get() = errorEmail == null &&
                errorPassword == null &&
                email.isNotEmpty() &&
                password.isNotEmpty()

}

