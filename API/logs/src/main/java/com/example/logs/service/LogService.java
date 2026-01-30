package com.example.logs.service;

import com.example.logs.dto.LogRequestDTO;
import com.example.logs.dto.LogResponseDTO;

import java.util.List;

public interface LogService {

    LogResponseDTO crearLog(LogRequestDTO request);
    List<LogResponseDTO> obtenerTodos();
}
