package com.example.poolcontrol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.poolcontrol.data.local.repository.UserRepository
import com.example.poolcontrol.data.repository.RolRepository

class AuthViewModelFactory(
    private val userRepository: UserRepository,
    private val rolRepository: RolRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Pasamos ambos repositorios al constructor del ViewModel
            return AuthViewModel(userRepository, rolRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}