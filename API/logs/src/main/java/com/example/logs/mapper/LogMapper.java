package com.example.logs.mapper;

import com.example.logs.dto.LogRequestDTO;
import com.example.logs.dto.LogResponseDTO;
import com.example.logs.model.Log;

import java.time.LocalDateTime;

public class LogMapper {

    public static Log toEntity(LogRequestDTO dto) {
        return Log.builder()
                .nivel(dto.getNivel())
                .mensaje(dto.getMensaje())
                .servicio(dto.getServicio())
                .fecha(LocalDateTime.now())
                .build();
    }

    public static LogResponseDTO toDTO(Log log) {
        return LogResponseDTO.builder()
                .id(log.getId())
                .nivel(log.getNivel())
                .mensaje(log.getMensaje())
                .servicio(log.getServicio())
                .fecha(log.getFecha())
                .build();
    }
}
