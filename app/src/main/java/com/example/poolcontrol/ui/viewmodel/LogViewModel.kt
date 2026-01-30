package com.example.poolcontrol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poolcontrol.data.ApiRepository.LogRepository
import com.example.poolcontrol.data.remote.dto.LogDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogViewModel(
    private val repository: LogRepository = LogRepository()
) : ViewModel() {

    private val _logs = MutableStateFlow<List<LogDTO>>(emptyList())
    val logs: StateFlow<List<LogDTO>> = _logs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarLogs() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _logs.value = repository.obtenerLogs()
            } catch (e: Exception) {
                _logs.value = emptyList() // podrías manejar error aquí
            } finally {
                _loading.value = false
            }
        }
    }
}
