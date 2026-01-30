package com.example.poolcontrol.data.ApiRepository

import com.example.poolcontrol.data.remote.LogApi
import com.example.poolcontrol.data.remote.RemoteModuleLog
import com.example.poolcontrol.data.remote.dto.LogDTO

class LogRepository {

    private val api: LogApi =
        RemoteModuleLog.retrofit.create(LogApi::class.java)

    suspend fun obtenerLogs(): List<LogDTO> {
        return api.obtenerLogs()
    }

    suspend fun crearLog(log: LogDTO) {
        api.crearLog(log)
    }
}
