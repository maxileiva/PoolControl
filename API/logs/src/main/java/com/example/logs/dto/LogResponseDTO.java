package com.example.logs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "DTO de respuesta para un log")
public class LogResponseDTO {

    @Schema(description = "ID del log", example = "1")
    private Long id;

    @Schema(description = "Nivel del log", example = "INFO")
    private String nivel;

    @Schema(description = "Mensaje del log", example = "El servicio inicio correctamente")
    private String mensaje;

    @Schema(description = "Servicio que generó el log", example = "logs")
    private String servicio;

    @Schema(description = "Fecha y hora en que se generó el log")
    private LocalDateTime fecha;
}
