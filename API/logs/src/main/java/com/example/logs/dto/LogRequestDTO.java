package com.example.logs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para crear un log")
public class LogRequestDTO {

    @NotBlank
    @Schema(description = "Nivel del log", example = "INFO")
    private String nivel;

    @NotBlank
    @Schema(description = "Mensaje del log", example = "El servicio inicio correctamente")
    private String mensaje;

    @NotBlank
    @Schema(description = "Nombre del servicio que genera el log", example = "logs")
    private String servicio;
}
