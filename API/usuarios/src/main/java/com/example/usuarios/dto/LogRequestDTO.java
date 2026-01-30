package com.example.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRequestDTO {

    @NotBlank
    private String nivel;

    @NotBlank
    private String mensaje;

    @NotBlank
    private String servicio;
}
