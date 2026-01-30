package com.example.logs.controller;

import com.example.logs.dto.LogResponseDTO;
import com.example.logs.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogController.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerLogs_deberiaRetornarLista() throws Exception {
        LogResponseDTO log = LogResponseDTO.builder()
                .id(1L)
                .nivel("INFO")
                .mensaje("Test log")
                .servicio("logs")
                .fecha(LocalDateTime.now())
                .build();

        when(logService.obtenerTodos()).thenReturn(List.of(log));

        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nivel").value("INFO"))
                .andExpect(jsonPath("$[0].mensaje").value("Test log"));
    }

    @Test
    void crearLog_deberiaCrearLog() throws Exception {
        LogResponseDTO response = LogResponseDTO.builder()
                .id(1L)
                .nivel("INFO")
                .mensaje("Creado")
                .servicio("logs")
                .fecha(LocalDateTime.now())
                .build();

        when(logService.crearLog(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        String json = """
                {
                  "nivel": "INFO",
                  "mensaje": "Creado",
                  "servicio": "logs"
                }
                """;

        mockMvc.perform(post("/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nivel").value("INFO"))
                .andExpect(jsonPath("$.mensaje").value("Creado"));
    }
}
