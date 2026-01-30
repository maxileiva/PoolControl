package com.example.logs.controller;

import com.example.logs.dto.LogRequestDTO;
import com.example.logs.dto.LogResponseDTO;
import com.example.logs.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "API para gestionar logs de servicios")
public class LogController {

    private final LogService logService;

    @PostMapping
    @Operation(
            summary = "Crear un nuevo log",
            description = "Crea un log con nivel, mensaje y servicio",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Log creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public LogResponseDTO crearLog(@Valid @RequestBody LogRequestDTO request) {
        return logService.crearLog(request);
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los logs",
            description = "Devuelve la lista completa de logs almacenados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de logs devuelta correctamente")
            }
    )
    public List<LogResponseDTO> obtenerLogs() {
        return logService.obtenerTodos();
    }
}
