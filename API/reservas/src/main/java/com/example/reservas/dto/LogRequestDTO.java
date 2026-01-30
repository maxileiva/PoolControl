package com.example.reservas.dto;
// en usuarios cambia el package

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogRequestDTO {

    @NotBlank
    private String nivel;

    @NotBlank
    private String mensaje;

    @NotBlank
    private String servicio;
}
